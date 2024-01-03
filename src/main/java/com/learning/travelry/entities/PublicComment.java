package com.learning.travelry.entities;

import java.math.BigInteger;
import java.sql.Timestamp;

public interface PublicComment {

    BigInteger getId();

    String getContent();

    Timestamp getCreated();

    String getProfilePhoto();

    String getUserName();

}
