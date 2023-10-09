package com.naveen.learning.services.impl;

import com.naveen.learning.dao.PasswordResetTokenDao;
import com.naveen.learning.dto.request.PasswordRequestToken;
import com.naveen.learning.model.PasswordResetToken;
import com.naveen.learning.model.User;
import com.naveen.learning.services.PasswordResetTokenService;
import com.naveen.learning.utils.constant.ErrorConstants;
import com.naveen.learning.utils.error.ErrorCodeHelper;
import com.naveen.learning.utils.error.response.ErrorInfo;
import com.naveen.learning.utils.error.response.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private static final Logger logger = Logger.getLogger(PasswordResetTokenServiceImpl.class);
    @Value(value = "${app.reset.password.token.duration}")
    private Long passwordResetTokenDurationInMs;

    @Autowired
    private PasswordResetTokenDao passwordResetTokenDao;

    @Autowired
    private ErrorCodeHelper errorCodeHelper;

    @Override
    public PasswordResetToken createResetToken(User user) {
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setActive(true);
        resetToken.setClaimed(false);
        resetToken.setExpiryDate(Instant.now().plusMillis(passwordResetTokenDurationInMs));
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken = passwordResetTokenDao.save(resetToken);
        return resetToken;
    }

    @Override
    public PasswordResetToken getValidResetToken(PasswordRequestToken requestToken) {
        PasswordResetToken passwordResetToken = passwordResetTokenDao.findByToken(requestToken.getToken());
        if (passwordResetToken == null) {
            logger.error("Password Reset Token Not Found");
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1006_ERROR_CODE,ErrorConstants.E1006_ERROR_DESCRIPTION);
            throw new ServiceException(errorInfo, HttpStatus.NOT_FOUND);
        }
        matchEmail(passwordResetToken, requestToken.getEmail());
        verifyExpiration(passwordResetToken);
        return passwordResetToken;
    }

    /**
     * Mark this reset password token as claimed (Used by user to update password)
     *
     */
    @Override
    public PasswordResetToken claimToken(PasswordResetToken passwordResetToken) {
        User user = passwordResetToken.getUser();
        passwordResetToken.setClaimed(true);
        passwordResetTokenDao.findActiveTokensForUser(user).forEach(t -> t.setActive(false));
        return passwordResetToken;
    }

    public void matchEmail(PasswordResetToken resetToken, String requestEmail) {
        if (!resetToken.getUser().getEmail().equalsIgnoreCase(requestEmail)) {
            logger.error("Password Reset Token is Invalid for User");
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1007_ERROR_CODE,ErrorConstants.E1007_ERROR_DESCRIPTION);
            throw new ServiceException(errorInfo, HttpStatus.NOT_FOUND);
        }
    }

    public void verifyExpiration(PasswordResetToken token) {
        if (token.getExpiryDate().isAfter(Instant.now())) {
            logger.error("Password Reset Token has expired");
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1008_ERROR_CODE,ErrorConstants.E1008_ERROR_DESCRIPTION);
            throw new ServiceException(errorInfo, HttpStatus.OK);
        }
        if (!token.isActive()) {
            logger.error("Token in Inactive");
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1009_ERROR_CODE,ErrorConstants.E1009_ERROR_DESCRIPTION);
            throw new ServiceException(errorInfo, HttpStatus.OK);
        }
    }
}
