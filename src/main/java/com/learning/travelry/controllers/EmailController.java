package com.learning.travelry.controllers;

import com.learning.travelry.entities.EmailDetails;
import com.learning.travelry.payload.request.OnlyEmailRequest;
import com.learning.travelry.payload.response.MessageResponse;
import com.learning.travelry.service.EmailService;
import com.learning.travelry.service.UserService;
import com.learning.travelry.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @PostMapping("/sendVerification")
    public ResponseEntity<?> sendEmailVerificationLink(@RequestBody OnlyEmailRequest details)
    {
        if (userService.existEmail(details.getEmail())) {
            String message = "Thanks for registering with us\n\n";
            message += "Click on below link for email verification link.\n\n";
            String token = JwtUtil.generateToken(details.getEmail());
            message += ("http://localhost:8080/api/auth/verify/"+token);

            EmailDetails emailDetails = new EmailDetails(
                    details.getEmail(), message, "Email Verification Needed!", null
            );

            String response = emailService.sendSimpleMail(emailDetails);
            if ("Error while Sending Mail".equals(response)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Something bad happen"));
            }

            return ResponseEntity.ok(new MessageResponse("Email Sent Successfully"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email not exists"));
        }
    }
}
