package com.learning.travelry.service;

import com.learning.travelry.entities.EmailDetails;

public interface EmailService {

     String sendSimpleMail(EmailDetails details);

}
