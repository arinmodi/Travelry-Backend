package com.learning.travelry.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Error: Email is already in use!")
public class UserEmailAlreadyExists extends RuntimeException {
}
