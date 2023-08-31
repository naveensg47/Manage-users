package com.naveen.learning.services;

import com.naveen.learning.dto.request.PasswordRequestToken;
import com.naveen.learning.model.PasswordResetToken;
import com.naveen.learning.model.User;

public interface PasswordResetTokenService {

    PasswordResetToken createResetToken(User user);

    PasswordResetToken getValidResetToken(PasswordRequestToken passwordRequestToken);

    PasswordResetToken claimToken(PasswordResetToken passwordResetToken);
}
