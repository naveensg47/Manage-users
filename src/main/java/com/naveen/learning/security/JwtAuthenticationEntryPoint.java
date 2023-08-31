package com.naveen.learning.security;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
custom implementation of JWT's AuthenticationEntryPoint.
This component is responsible for handling unauthenticated or unauthorized requests and providing an appropriate response.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = Logger.getLogger(JwtAuthenticationEntryPoint.class);

    /*If an exception is found, it uses the HandlerExceptionResolver to handle that exception
     and generate an appropriate response.*/
    private final HandlerExceptionResolver resolver;

    @Autowired
    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("User is UnAuthorized. routing from the entry point.");

        // Handling an exception if it's available as a request attribute
        if (request.getAttribute("javax.servlet.error.exception") != null) {
            Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
            resolver.resolveException(request, response, null, (Exception) throwable);
        }

        // if exception is not detected,Sending an error response with HTTP 401 Unauthorized
        if (!response.isCommitted()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        }
    }
}
