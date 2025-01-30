package com.wn.wandernest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wn.wandernest.dtos.ApiResponseDTO;
import com.wn.wandernest.dtos.ApiResponseDTO.Status;
import com.wn.wandernest.models.Role;
import com.wn.wandernest.models.User;
import com.wn.wandernest.security.TokenBlacklist;
import com.wn.wandernest.services.UserService;
import com.wn.wandernest.utils.JwtTokenUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklist tokenBlacklist;

    public AuthController(AuthenticationManager authenticationManager,
            UserService userService,
            JwtTokenUtil jwtTokenUtil,
            PasswordEncoder passwordEncoder,
            TokenBlacklist tokenBlacklist) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.tokenBlacklist = tokenBlacklist;
    }

    // Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(Status.ERROR, "Username is already taken!", null));
        }
        if (registerRequest.getUsername() == null || registerRequest.getUsername().isEmpty() ||
                registerRequest.getEmail() == null || registerRequest.getEmail().isEmpty() ||
                registerRequest.getPassword() == null || registerRequest.getPassword().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(Status.ERROR, "Missing username, email, or password!", null));
        }
        // validate username
        String username = registerRequest.getUsername();
        if (username.length() < 3 || username.length() > 20 || !username.matches("^[a-zA-Z0-9._]+$")) {
            return ResponseEntity.badRequest().body(
                    new ApiResponseDTO(Status.ERROR,
                            "Username must be 3-20 characters long and can only contain letters, numbers, underscores, and periods.",
                            null));
        }
        // validate password
        String password = registerRequest.getPassword();
        if (password.length() < 8 || password.length() > 16 ||
                !password.matches(".*[A-Z].*") ||
                !password.matches(".*[a-z].*") ||
                !password.matches(".*[0-9].*") ||
                !password.matches(".*[@#$%^&*()_+!].*") ||
                password.contains(" ") ||
                password.equalsIgnoreCase("123456") ||
                password.equalsIgnoreCase("password") ||
                password.equalsIgnoreCase("qwerty")) {
            return ResponseEntity.badRequest().body(
                    new ApiResponseDTO(Status.ERROR,
                            "Password must be 8-16 characters long, contain at least one uppercase letter, one lowercase letter, one number, one special character, and should not contain spaces or be a common password.",
                            null));
        }
        // Validate email format
        if (!registerRequest.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return ResponseEntity.badRequest().body(
                    new ApiResponseDTO(Status.ERROR, "Invalid email format!", null));
        }
        // Check if email already exists
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(Status.ERROR, "Email is already taken!", null));
        }
        // Create a new user and set its properties
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRole(Role.ROLE_USER); // Default role

        // Save the new user
        userService.save(user);
        RegisterResponse registerResponse = new RegisterResponse(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity
                .ok(new ApiResponseDTO(Status.SUCCESS, "User registered successfully!", registerResponse));
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }
    // Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO(Status.ERROR, "Token is missing!", null));
        }
        tokenBlacklist.addToBlacklist(token);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ApiResponseDTO(Status.SUCCESS, "User logged out successfully!", null));
    }
    // DTO Classes
    @Getter
    @Setter
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class LoginResponse {
        private String token;
    }

    @Getter
    @Setter
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class RegisterResponse {
        private Long id;
        private String username;
        private String email;

    }
}
