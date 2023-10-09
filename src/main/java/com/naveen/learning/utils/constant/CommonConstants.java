package com.naveen.learning.utils.constant;

import java.util.regex.Pattern;

public class CommonConstants {

    public static final String S0001_SUCCESS_CODE = "SM-HTTP-CODE-S0001";
    public static final String S0001_SUCCESS_DESCRIPTION = "SM-HTTP-DESCRIPTION-S0001";
    public static final String TOKEN ="token" ;
    public static final String ADD_USER ="add" ;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    public static final String ERROR_PROPERTIES="classpath:error.properties";
    public static final String LOGIN = "login";
    public static final String USER = "user";
    public static final String ID = "id";
    public static final String GET_USER = "user";
}
