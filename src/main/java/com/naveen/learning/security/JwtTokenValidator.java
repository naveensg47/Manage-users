package com.naveen.learning.security;

import com.naveen.learning.utils.constant.ErrorConstants;
import com.naveen.learning.utils.error.ErrorCodeHelper;
import com.naveen.learning.utils.error.response.ErrorInfo;
import com.naveen.learning.utils.error.response.ServiceException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenValidator {

    private static final Logger logger = Logger.getLogger(JwtTokenValidator.class);

    private final String secret;

    @Autowired
    private ErrorCodeHelper errorCodeHelper;


    public JwtTokenValidator(@Value("${app.jwt.secret}") String secret) {
        this.secret = secret;
    }

    /*
     *Validate token if it satisfies following properties
     * -Signature is not malformed
     * -Token hasn't expired
     * -Token is supported
     */

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJwt(authToken);
        } catch (SignatureException e) {
            logger.error("Invalid Jwt Signature");
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo(ErrorConstants.E1002_ERROR_CODE, ErrorConstants.E1001_ERROR_DESCRIPTION, "Signature");
            throw new ServiceException(errorInfo, HttpStatus.OK);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            logger.error("Token is expired");
        } catch (UnsupportedJwtException e) {
            logger.error("UnSupported JWT token");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty");
        }
//1.validate token from cache
        //2.validate token from database
        return true;
    }
}
