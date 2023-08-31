package com.naveen.learning.services;

import com.naveen.learning.dto.response.UserDto;
import com.naveen.learning.model.User;

import java.util.Optional;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, Long userId);

    UserDto findUserById(Long userId);

    Optional<User> findByEmail(String email);

    boolean isEmailExists(String email);

    User save(User user);
}
