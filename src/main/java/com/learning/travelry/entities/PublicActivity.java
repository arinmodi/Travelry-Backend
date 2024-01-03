package com.learning.travelry.entities;

import java.math.BigInteger;
import java.sql.Timestamp;

public interface PublicActivity {

    BigInteger getId();

    Timestamp getCreated();

    String getMessage();

    String getDiaryName();

    String getUserName();

    String getProfilePhoto();

}
