package com.naveen.learning.services.impl;

import com.naveen.learning.dto.request.PasswordRequestToken;
import com.naveen.learning.dto.response.TokenResponse;
import com.naveen.learning.model.CustomUserDetails;
import com.naveen.learning.model.PasswordResetToken;
import com.naveen.learning.model.RefreshToken;
import com.naveen.learning.model.User;
import com.naveen.learning.security.JwtTokenProvider;
import com.naveen.learning.services.AuthService;
import com.naveen.learning.services.PasswordResetTokenService;
import com.naveen.learning.services.RefreshTokenService;
import com.naveen.learning.services.UserService;
import com.naveen.learning.utils.constant.ErrorConstants;
import com.naveen.learning.utils.error.ErrorCodeHelper;
import com.naveen.learning.utils.error.response.ErrorInfo;
import com.naveen.learning.utils.error.response.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;


@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = Logger.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ErrorCodeHelper errorCodeHelper;

    //Authenticates the user
    @Override
    public TokenResponse authenticateUser(String userMail, String password) {
        Optional<User> user=userService.findByEmail(userMail);
        if (!user.isPresent()) {
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo("E1004", "Email Not Exists");
            throw new ServiceException(errorInfo, HttpStatus.FOUND);
        }
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userMail, password));
        } catch (BadCredentialsException e) {
            //e.printStackTrace();
            logger.error("Authentication Failed");
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1004_ERROR_CODE,ErrorConstants.E1004_ERROR_DESCRIPTION);
            throw new ServiceException(errorInfo, HttpStatus.FORBIDDEN);
        }
        logger.info("User Authenticated");
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        logger.info("User logged in is : " + customUserDetails.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        RefreshToken refreshToken = createAndPersistRefreshToken(user.get(), userMail, password);
        if (refreshToken == null) {
            logger.error("Failed to create a refresh token");
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1010_ERROR_CODE,ErrorConstants.E1010_ERROR_DESCRIPTION);
            throw new ServiceException(errorInfo,HttpStatus.OK);
        }
        String jwtToken = jwtTokenProvider.generateToken(customUserDetails);
        return new TokenResponse(jwtToken, refreshToken.getToken(), Instant.now().plusMillis(jwtTokenProvider.getJwtExpirationInMs()));
    }

    private RefreshToken createAndPersistRefreshToken(User currentUser, String userMail, String password) {
        RefreshToken refreshToken = refreshTokenService.createRefreshtoken(currentUser);
        refreshToken = refreshTokenService.save(refreshToken);
        return refreshToken;

    }

    @Override
    public String logout(Long userId) {
        return null;
    }

    @Override
    public PasswordResetToken resetPasswordToken(String email) {
        Optional<User> user = userService.findByEmail(email);
        if (!user.isPresent()) {
            logger.error("Email doesn't exist");
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1005_ERROR_CODE,ErrorConstants.E1005_ERROR_DESCRIPTION);
            throw new ServiceException(errorInfo, HttpStatus.NOT_FOUND);
        }
        PasswordResetToken passwordResetToken = passwordResetTokenService.createResetToken(user.get());
        return passwordResetToken;
    }

    @Override
    public Optional<User> resetPassword(PasswordRequestToken passwordRequestToken) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.getValidResetToken(passwordRequestToken);
        String encodedPassword = passwordEncoder.encode(passwordRequestToken.getPassword());
        return Optional.of(passwordResetToken)
                .map(passwordResetTokenService::claimToken)
                .map(PasswordResetToken::getUser)
                .map(user -> {
                    user.setPassword(encodedPassword);
                    userService.save(user);
                    return user;
                });
    }

    //Refresh the JWT token by refresh token
    @Override
    public String refreshJwtToken(String refreshToken) {
        RefreshToken token = refreshTokenService.findByToken(refreshToken);
        return Optional.of(token)
                .map(RefreshToken::getUser)
                .map(CustomUserDetails::new)
                .map(jwtTokenProvider::generateToken)
                .get();  //else throw n error

    }
}
