package com.naveen.learning.services;

import com.naveen.learning.model.UserToken;

public interface UserTokenService {

boolean storeToken(UserToken userToken);
UserTokenService getTokenByUserId(Long id);

boolean updateToken(UserToken userToken);

UserToken getUserTokenByToken(String token);

void deleteToken(UserToken userToken);



}
