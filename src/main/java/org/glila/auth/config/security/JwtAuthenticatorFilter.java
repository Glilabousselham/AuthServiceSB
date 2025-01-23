package org.glila.auth.config.security;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.glila.auth.modules.auth.service.JWTUtilService;
import org.glila.auth.modules.user.entity.User;
import org.glila.auth.modules.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;



public class JwtAuthenticatorFilter extends OncePerRequestFilter {

    private final UserDetailsService customUserDetailsService;

    public JwtAuthenticatorFilter(UserDetailsService customUserDetailsService){
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String authHeader = request.getHeader("Authorization");


        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);
            try {
                String username = JWTUtilService.extractUsername(token);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // inject the user details into the security context holder
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );


                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                throw new AccessDeniedException("Unauthorized request");
            }


        }

        // call next filter
        filterChain.doFilter(request, response);

    }
}
