package com.naveen.learning.services;

import com.naveen.learning.dao.PasswordResetTokenDao;
import com.naveen.learning.dto.request.PasswordRequestToken;
import com.naveen.learning.dto.response.TokenResponse;
import com.naveen.learning.model.PasswordResetToken;
import com.naveen.learning.model.User;

import java.util.Optional;

public interface AuthService {

    TokenResponse authenticateUser(String userMail, String password);
    String logout(Long userId);

    PasswordResetToken resetPasswordToken(String email);

    Optional<User> resetPassword(PasswordRequestToken passwordRequestToken);

    String refreshJwtToken(String refreshToken);
}
