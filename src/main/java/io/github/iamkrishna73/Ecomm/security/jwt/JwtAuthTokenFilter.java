package io.github.iamkrishna73.Ecomm.security.jwt;

import io.github.iamkrishna73.Ecomm.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {
    public static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    public JwtAuthTokenFilter(CustomUserDetailsService userDetailsService, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        logger.debug("JwtAuthTokenFilter called for uri: {}", request.getRequestURI());
        try {
            String jwtToken = parseJwtToken(request);
            if (jwtToken != null && jwtUtils.validateToken(jwtToken)) {
                String username = jwtUtils.getUserNameFromToken(jwtToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                logger.debug("Roles form Jwt: {}: ", userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception exception) {
            logger.error("Can not set the user authentication: ", exception);

        }
        filterChain.doFilter(request, response);

    }

    private String parseJwtToken(HttpServletRequest request) {
        String jwtToken = jwtUtils.getJwtTokenFromHeader(request);
        logger.debug("JwtAuthTokenFilter:parseJwtToken: {}", jwtToken);
        return jwtToken;
    }
}
