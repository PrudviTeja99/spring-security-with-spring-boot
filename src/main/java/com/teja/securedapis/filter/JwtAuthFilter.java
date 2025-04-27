package com.teja.securedapis.filter;

import com.teja.securedapis.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtil jwtUtil,UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService=userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String bearerToken = null;
        if(authorizationHeader!=null && !authorizationHeader.isBlank()){
            bearerToken = authorizationHeader.substring(7);
        }
        if(bearerToken!=null && !bearerToken.isBlank()){
            String username = jwtUtil.fetchUsername(bearerToken);
            if(username!=null && !username.isBlank()){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if(jwtUtil.isTokenValid(bearerToken,userDetails)){
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
