package com.learning.travelry.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

      @NotBlank
      @Size(max = 50)
      @Email
      private String email;

      @NotBlank
      @Size(min = 6, max = 40)
      private String password;

      public String getEmail() {
        return email;
      }

      public void setEmail(String email) {
        this.email = email;
      }

      public String getPassword() {
        return password;
      }

      public void setPassword(String password) {
        this.password = password;
      }

}
