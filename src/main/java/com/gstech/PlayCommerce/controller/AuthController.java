package com.gstech.PlayCommerce.controller;


import com.gstech.PlayCommerce.dto.LoginRequest;
import com.gstech.PlayCommerce.dto.LoginResponse;
import com.gstech.PlayCommerce.security.JWTService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetails user = userDetailsService.loadUserByUsername(request.username());
        String tokenJWT = jwtService.generateTokenJWT(user);

        return ResponseEntity.ok(new LoginResponse(tokenJWT, 86400000L));
    }
}
