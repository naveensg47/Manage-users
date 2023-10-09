package com.naveen.learning.services.impl;

import com.naveen.learning.dao.EnumItemDao;
import com.naveen.learning.dao.RoleDao;
import com.naveen.learning.dao.UserDao;
import com.naveen.learning.dto.response.UserDto;
import com.naveen.learning.model.Role;
import com.naveen.learning.model.User;
import com.naveen.learning.services.UserService;
import com.naveen.learning.utils.ValidationUtils;
import com.naveen.learning.utils.constant.ErrorConstants;
import com.naveen.learning.utils.error.ErrorCodeHelper;
import com.naveen.learning.utils.error.response.ErrorInfo;
import com.naveen.learning.utils.error.response.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private EnumItemDao enumItemDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ErrorCodeHelper errorCodeHelper;


    @Override
    public UserDto addUser(UserDto userDto) {
        validateUserDto(userDto);
        User user = new User();
        Optional<User> optionalUser = userDao.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent()) {
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1003_ERROR_CODE, ErrorConstants.E1003_ERROR_DESCRIPTION, userDto.getEmail());
            throw new ServiceException(errorInfo, HttpStatus.OK);
        }
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setActive(true);
        user.setEmailVerified(true);
        user.setRoles(new HashSet<>());
        return addOrUpdateUser(userDto, user);
    }

    private void validateUserDto(UserDto userDto){
        ValidationUtils.validateEmail(userDto.getEmail());
        ValidationUtils.validateUserPassword(userDto.getPassword());
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        User existingUser = userDao.findById(userId).filter(u -> !u.isActive()).orElseThrow(() -> {
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1002_ERROR_CODE, ErrorConstants.E1002_ERROR_DESCRIPTION, "User");
            return new ServiceException(errorInfo);
        });
        return addOrUpdateUser(userDto, existingUser);
    }

    public UserDto addOrUpdateUser(UserDto userDto, User user) {
        user.setUsername(userDto.getUsername());
        user.setMobileNumber(userDto.getMobileNumber());
        setUserRoles(userDto.getRoleIds(), user);
        user = userDao.save(user);
        return userDto.mapUserDto(user);
    }

    private void setUserRoles(List<Long> roleIds, User user) {
        List<Role> roles = roleDao.findRoleByIds(roleIds);
        if (CollectionUtils.isEmpty(roles)) {
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1002_ERROR_CODE, ErrorConstants.E1002_ERROR_DESCRIPTION, "Roles");
            throw new ServiceException(errorInfo);
        }
        Set<Role> userRoles = user.getRoles();
        userRoles.clear();
        for (Role role : roles) {
         user.addRole(role);
        }
        user.setRoles(userRoles);
    }

    @Override
    public UserDto findUserById(Long userId) {
        User existingUser = userDao.findById(userId).orElseThrow(() -> {
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1002_ERROR_CODE, ErrorConstants.E1002_ERROR_DESCRIPTION, "User");
            return new ServiceException(errorInfo);
        });
        return new UserDto().mapUserDto(existingUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userDao.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return userDao.save(user);
    }
}
