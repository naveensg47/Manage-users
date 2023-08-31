package com.naveen.learning.services.impl;

import com.naveen.learning.dao.UserDao;
import com.naveen.learning.model.CustomUserDetails;
import com.naveen.learning.model.User;
import com.naveen.learning.security.JwtAuthenticationFilter;
import com.naveen.learning.utils.constant.ErrorConstants;
import com.naveen.learning.utils.error.ErrorCodeHelper;
import com.naveen.learning.utils.error.response.ErrorInfo;
import com.naveen.learning.utils.error.response.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class);

    final private UserDao userDao;
    final private ErrorCodeHelper errorCodeHelper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByEmail(username).orElseThrow(() -> {
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1001_ERROR_CODE, ErrorConstants.E1001_ERROR_DESCRIPTION, username);
            logger.error(String.format("User with email %s not found",username));
            return new ServiceException(errorInfo, HttpStatus.OK);
        });

        return new CustomUserDetails(user);
    }

    private Set<SimpleGrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName().name())));
        return authorities;
    }

    public UserDetails loadUserByUserId(Long userId) {
        User user = userDao.findById(userId).orElseThrow(() -> {
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1001_ERROR_CODE, ErrorConstants.E1001_ERROR_DESCRIPTION, "userId");
            logger.error(String.format("User with id %s not found",userId));
            return new ServiceException(errorInfo, HttpStatus.OK);
        });

        return new CustomUserDetails(user);
    }
}
