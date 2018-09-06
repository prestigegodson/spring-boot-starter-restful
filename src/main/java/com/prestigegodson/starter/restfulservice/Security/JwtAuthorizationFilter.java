package com.prestigegodson.starter.restfulservice.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prestigegodson.starter.restfulservice.error.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by prestige on 8/28/2018.
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter{

    private JwtTokenProvider tokenProvider;
    private UserDetailsServiceImp userDetailsServiceImp;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    public JwtAuthorizationFilter(ApplicationContext context) {
        tokenProvider = context.getBean(JwtTokenProvider.class);
        userDetailsServiceImp = context.getBean(UserDetailsServiceImp.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromJWT(jwt);

                logger.info("Token valid, User id : " + userId);
                UserDetails userDetails = userDetailsServiceImp.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            logger.error("Could not set user authentication in security context", ex);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setTimestamp(new Date());
            errorResponse.setStatus(4001);
            errorResponse.setError("UnAuthorized");
            errorResponse.setMessage("Token Expired");

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), errorResponse);
        } catch (Exception ex) {

            logger.error("Could not set user authentication in security context", ex);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setTimestamp(new Date());
            errorResponse.setStatus(4000);
            errorResponse.setError("UnAuthorized");
            errorResponse.setMessage(ex.getMessage());

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), errorResponse);
        }

    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
