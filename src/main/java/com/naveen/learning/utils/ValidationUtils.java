package com.naveen.learning.utils;

import com.naveen.learning.utils.constant.CommonConstants;
import com.naveen.learning.utils.error.ErrorCodeHelper;
import com.naveen.learning.utils.error.response.ErrorInfo;
import com.naveen.learning.utils.error.response.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.regex.Matcher;

@Component
public class ValidationUtils {

    @Autowired
    private static ErrorCodeHelper errorCodeHelper;
    private static final Logger logger = Logger.getLogger(ValidationUtils.class);

    public static void validateEmail(String userMail) {
        Matcher matcher = CommonConstants.VALID_EMAIL_ADDRESS_REGEX.matcher(userMail);
        if (!matcher.find()) {
            logger.info("Email validation failed");
            ErrorInfo errorInfo=errorCodeHelper.getErrorInfo("E2001","Token has Expired");
            throw new ServiceException(errorInfo, HttpStatus.OK);
        }
    }

    public void validateTokenExpiration(Instant expiration){
        if (expiration.isAfter(Instant.now())){
            logger.info("Token has expired");
            ErrorInfo errorInfo=errorCodeHelper.getErrorInfo("E2002","Token has Expired");
            throw new ServiceException(errorInfo, HttpStatus.OK);
        }
    }

    public static void validateUserPassword(String field){
        if (StringUtils.isEmpty(field) || StringUtils.length(field)<=8){
            logger.error("User password length is not matching");
            ErrorInfo errorInfo=errorCodeHelper.getErrorInfo("E2003","Password should be minimum 8 characters");
            throw new ServiceException(errorInfo, HttpStatus.OK);
        }

    }
}
