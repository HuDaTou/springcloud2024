package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.dto.LoginRequest;
import com.overthinker.cloud.auth.service.impl.UserServiceImpl;
import com.overthinker.cloud.auth.utils.JwtUtils;

import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for user authentication.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private UserServiceImpl userService;

    @PostMapping("/login")
    public ResultData<String> login(@RequestBody LoginRequest loginRequest) {
        // Use Spring Security's AuthenticationManager to validate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // Assuming LoginUser contains the user ID
        // This part needs to be adapted based on your LoginUser implementation
        Long userId = 1L; // Placeholder, you need to get the real user ID

        // Create JWT
        String jwt = jwtUtils.createJwt(userDetails, userId, userDetails.getUsername());

        return ResultData.success(jwt);
    }

    @PostMapping("/internal/api/v1/auth/verify")
    public ResultData<Void> verify(String token, String url) {
        DecodedJWT decodedJWT = jwtUtils.resolveJwt(token);
        if (decodedJWT == null) {
            return ResultData.failure("Invalid token");
        }

        Long userId = jwtUtils.toId(decodedJWT);
        List<String> userAuthorities = userService.getUserAuthorities(userId);

        // This is a simplified check. In a real system, you would look up the required
        // permission for the given URL from the database.
        boolean hasPermission = userAuthorities.stream().anyMatch(url::contains);

        if (hasPermission) {
            return ResultData.success();
        } else {
            return ResultData.failure("No permission");
        }
    }
}
