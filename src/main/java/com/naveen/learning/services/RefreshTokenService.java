package com.naveen.learning.services;

import com.naveen.learning.model.RefreshToken;
import com.naveen.learning.model.User;

public interface RefreshTokenService {

    RefreshToken createRefreshtoken(User user);

    RefreshToken save(RefreshToken refreshToken);

    RefreshToken findByToken(String refreshToken);
}
