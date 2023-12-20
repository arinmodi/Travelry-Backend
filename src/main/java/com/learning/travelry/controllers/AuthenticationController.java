package com.learning.travelry.controllers;

import com.learning.travelry.entities.User;
import com.learning.travelry.payload.request.LoginRequest;
import com.learning.travelry.payload.request.SignupRequest;
import com.learning.travelry.payload.response.LoginResponse;
import com.learning.travelry.payload.response.MessageResponse;
import com.learning.travelry.service.UserService;
import com.learning.travelry.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userService.existEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        User user = new User(signUpRequest.getEmail(), signUpRequest.getUsername(), false,
                encryptedPassword);

        userService.save(user);

        return ResponseEntity.ok(new MessageResponse("User Registered successfully"));
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<?> verifyEmail(
            @PathVariable(value="token") String token
    ) {
        String response = JwtUtil.extractEmail(token);

        if (response == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid token!"));
        } else {
            if (userService.markUserVerified(response)) {
                return ResponseEntity.ok(new MessageResponse("Successfully verified"));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Something Bad Happen!"));
            }
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        if (!userService.existEmail(loginRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User Not Exists!"));
        }

        User user = userService.getUser(loginRequest.getEmail());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = JwtUtil.generateTokenForLogin(loginRequest.getEmail());
            return ResponseEntity.ok(new LoginResponse("Successfully LoggedIn", token));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid credentials!"));
        }
    }
}
