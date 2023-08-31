package com.naveen.learning.services.impl;

import com.naveen.learning.dao.RefreshTokenDao;
import com.naveen.learning.model.RefreshToken;
import com.naveen.learning.model.User;
import com.naveen.learning.services.RefreshTokenService;
import com.naveen.learning.utils.error.ErrorCodeHelper;
import com.naveen.learning.utils.error.response.ErrorInfo;
import com.naveen.learning.utils.error.response.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/*
   *1.Create a token
   * 2.verify the token expiration
   *
 */

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${app.token.refresh.duration}")
    private Long refreshTokenDurationMs;
    @Autowired
    private RefreshTokenDao refreshTokenDao;

  private ErrorCodeHelper errorCodeHelper;

    //Create a refreshToken
    @Override
    public RefreshToken createRefreshtoken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRefreshCount(0L);
        return refreshToken;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenDao.save(refreshToken);
    }

    @Override
    public RefreshToken findByToken(String refreshToken) {
        RefreshToken token= refreshTokenDao.findByToken(refreshToken);
        if (token==null){
            throw new RuntimeException("RefreshToken Not Fount");
        }
        verifyRefreshTokenExpiration(token);
        increaseCount(token);
        return token;
    }

    private void increaseCount(RefreshToken token) {
        token.setRefreshCount(token.getRefreshCount()+1);
        save(token);
    }

    //Verify the expiration of refresh token
    public void verifyRefreshTokenExpiration(RefreshToken refreshToken){
        if (refreshToken.getExpiryDate().isBefore(Instant.now())){
            ErrorInfo errorInfo=errorCodeHelper.getErrorInfo("E1007","Refresh token is Expired");
            throw new ServiceException(errorInfo, HttpStatus.UNAUTHORIZED);
        }
    }



}
