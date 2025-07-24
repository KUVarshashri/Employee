package com.example.employeecrud.controller;

import com.example.employeecrud.dao.Employees;
import com.example.employeecrud.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Employees employees){
        try {
            Authentication authentication= authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(employees.getEmail(),employees.getPassword()));
            UserDetails userDetails= (UserDetails) authentication.getPrincipal();
            String token= jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(Map.of("token",token));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Invalid username or password"));
        }
    }
}
