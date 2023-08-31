package com.naveen.learning.controller;

import com.naveen.learning.dto.response.UserDto;
import com.naveen.learning.services.UserService;
import com.naveen.learning.utils.ValidationUtils;
import com.naveen.learning.utils.constant.CommonConstants;
import com.naveen.learning.utils.response.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private ResponseJson response;
    @Autowired
    private UserService userService;

    @Autowired
    private ValidationUtils validationUtils;

    @GetMapping(value = CommonConstants.TOKEN)
    public ResponseJson createToken(@RequestParam String userMail, @RequestParam String password) {
        validationUtils.validateEmail(userMail);
        return null;
    }

    @PostMapping(value = CommonConstants.ADD_USER)
    public ResponseJson createUser(@Valid @RequestBody UserDto userDto) {
        validationUtils.validateEmail(userDto.getEmail());
        response.setResponseDescription(CommonConstants.S0001_SUCCESS_DESCRIPTION);
        response.setResponse(userService.addUser(userDto));
        return response;
    }

    @GetMapping(value = CommonConstants.GET_USER)
    public ResponseJson getUserById(@PathVariable Long id){
       response.setResponseDescription(CommonConstants.S0001_SUCCESS_DESCRIPTION);
       response.setResponse(userService.findUserById(id));
       return response;
    }

}
