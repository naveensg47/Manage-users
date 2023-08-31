package com.naveen.learning.security;

import com.naveen.learning.services.impl.CustomerUserDetailsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// We should use OncePerRequestFilter since we are doing a database call, there is no point in doing this more than once
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = Logger.getLogger(JwtAuthenticationFilter.class);

    @Value("${app.jwt.header}")
    private String tokenRequestHeader;

    @Value("${app.jwt.header.prefix}")
    private String tokenRequestHeaderPrefix;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtTokenValidator jwtTokenValidator;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && jwtTokenValidator.validateJwtToken(jwt)) {
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                UserDetails userDetails = customerUserDetailsService.loadUserByUserId(userId);
                List<GrantedAuthority> authorities = jwtTokenProvider.getAuthoritiesFromToken(jwt);

                //Setting values to UsernamePasswordAuthenticationToken instance through constructor
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, jwt, authorities);

                //Loading the instance using WebAuthenticationDetailsSource, which encapsulates request data
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            //this is very important, since it guarantees the user is not authenticated at all
            SecurityContextHolder.clearContext();
            logger.error("Failed to set user authentication in security context:", e);
            throw e;
        }
        filterChain.doFilter(request,response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(tokenRequestHeader);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenRequestHeaderPrefix)) {
            logger.info("Extracted token:" + bearerToken);
            return bearerToken.replace(tokenRequestHeaderPrefix, "");
        }
        return null;
    }
}
