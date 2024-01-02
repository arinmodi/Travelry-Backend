package com.learning.travelry.entities;

import java.math.BigInteger;
import java.sql.Timestamp;

public interface PublicMedia {

    BigInteger getId();

    String getURL();

    String getName();

    String getOwner();

    Boolean getIsVideo();

    Timestamp getCreated();

}
