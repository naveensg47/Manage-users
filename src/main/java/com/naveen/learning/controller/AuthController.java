package com.naveen.learning.controller;

import com.naveen.learning.dto.request.PasswordRequestToken;
import com.naveen.learning.dto.response.TokenResponse;
import com.naveen.learning.event.OnPasswordResetLinkEvent;
import com.naveen.learning.event.OnUserAccountChangeEvent;
import com.naveen.learning.model.PasswordResetToken;
import com.naveen.learning.model.User;
import com.naveen.learning.security.JwtTokenProvider;
import com.naveen.learning.services.AuthService;
import com.naveen.learning.utils.constant.CommonConstants;
import com.naveen.learning.utils.response.ResponseJson;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("auth/v1")
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class);

    @Autowired
    private ResponseJson response;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    //Authenticate User
    @GetMapping(value = CommonConstants.LOGIN)
    @ApiOperation(value = "User logs in and get the authentication token in return")
    public ResponseJson authenticateUser(@RequestParam String userEmail, @RequestParam String password) {
        response.setResponseDescription(CommonConstants.S0001_SUCCESS_DESCRIPTION);
        response.setResponse(authService.authenticateUser(userEmail, password));
        return response;
    }

    //Send Reset password Link
    @GetMapping(value = "password/resetLink")
    public ResponseJson sendResetPasswordLink(@RequestParam @Valid String email) {
        //create a token and send it through email
        PasswordResetToken passwordResetToken = authService.resetPasswordToken(email);
        UriComponentsBuilder redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/password/reset");
        applicationEventPublisher.publishEvent(new OnPasswordResetLinkEvent(passwordResetToken, redirectUrl));

        response.setResponseDescription(CommonConstants.S0001_SUCCESS_DESCRIPTION);
        response.setResponse("Password Reset Link Sent Successfully");
        return response;
    }

    //Reset Password
    @PostMapping(value = "password/reset")
    public ResponseJson resetPassword(@RequestBody @Valid PasswordRequestToken passwordRequestToken) {
        Optional<User> user = authService.resetPassword(passwordRequestToken);
        OnUserAccountChangeEvent onUserAccountChangeEvent = new OnUserAccountChangeEvent(user.get(), "Reset Password", "Changed Successfully");
        applicationEventPublisher.publishEvent(onUserAccountChangeEvent);
        response.setResponseDescription(CommonConstants.S0001_SUCCESS_DESCRIPTION);
        response.setResponse("Password Changed successfully");
        return response;
    }

    //generate Jwt Token using refresh token if existing token expires
    @GetMapping(value = "/token/refresh")
    public ResponseJson refreshJwtToken(@RequestParam String refreshToken) {
        String token = authService.refreshJwtToken(refreshToken);
        response.setResponseDescription(CommonConstants.S0001_SUCCESS_DESCRIPTION);
        response.setResponse(new TokenResponse(token, refreshToken, Instant.now().plusMillis(jwtTokenProvider.getJwtExpirationInMs())));
        return response;
    }

}
