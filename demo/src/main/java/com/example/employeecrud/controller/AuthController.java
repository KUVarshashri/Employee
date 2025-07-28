package com.example.employeecrud.controller;

import com.example.employeecrud.dao.Employees;
import com.example.employeecrud.repository.EmployeesRepo;
import com.example.employeecrud.security.JwtUtil;
import com.example.employeecrud.service.ServiceImpl.CustomUserDetailsServiceImpl;
import com.example.employeecrud.dto.GenericResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final String LOGIN_SUCCESS = "Login successful";
    private static final String INVALID_CREDENTIALS = "Invalid username or password";
    private static final String REFRESH_SUCCESS = "Token refreshed successfully";
    private static final String REFRESH_REQUIRED = "Refresh token is required";
    private static final String REFRESH_INVALID = "Invalid refresh token";
    private static final String USER_NOT_FOUND = "User not found";

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmployeesRepo employeesRepo;

    @Autowired
    @Qualifier("customUserDetailsService")
    private CustomUserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public GenericResponseEntity<Map<String, String>> login(@RequestBody Employees employees) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(employees.getEmail(), employees.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Employees dbEmployee = employeesRepo.findByEmail(employees.getEmail())
                    .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

            String accessToken = jwtUtil.generateToken(userDetails, dbEmployee);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            Map<String, String> tokens = Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            );

            return GenericResponseEntity.<Map<String, String>>builder()
                    .message(LOGIN_SUCCESS)
                    .data(tokens)
                    .success(true)
                    .statusCode(200)
                    .status("OK")
                    .build();

        } catch (Exception e) {
            return GenericResponseEntity.<Map<String, String>>builder()
                    .message(INVALID_CREDENTIALS)
                    .data(null)
                    .success(false)
                    .statusCode(401)
                    .status("UNAUTHORIZED")
                    .build();
        }
    }

    @PostMapping("/refresh")
    public GenericResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");

        if (refreshToken == null || refreshToken.isEmpty()) {
            return GenericResponseEntity.<Map<String, String>>builder()
                    .message(REFRESH_REQUIRED)
                    .data(null)
                    .success(false)
                    .statusCode(400)
                    .status("BAD_REQUEST")
                    .build();
        }

        try {
            String username = jwtUtil.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(refreshToken, userDetails)) {
                Employees dbEmployee = employeesRepo.findByEmail(username)
                        .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

                String newAccessToken = jwtUtil.generateToken(userDetails, dbEmployee);

                Map<String, String> tokens = Map.of(
                        "accessToken", newAccessToken,
                        "refreshToken", refreshToken
                );

                return GenericResponseEntity.<Map<String, String>>builder()
                        .message(REFRESH_SUCCESS)
                        .data(tokens)
                        .success(true)
                        .statusCode(200)
                        .status("OK")
                        .build();
            } else {
                return GenericResponseEntity.<Map<String, String>>builder()
                        .message(REFRESH_INVALID)
                        .data(null)
                        .success(false)
                        .statusCode(403)
                        .status("FORBIDDEN")
                        .build();
            }

        } catch (Exception e) {
            return GenericResponseEntity.<Map<String, String>>builder()
                    .message(REFRESH_INVALID)
                    .data(null)
                    .success(false)
                    .statusCode(403)
                    .status("FORBIDDEN")
                    .build();
        }
    }
}
