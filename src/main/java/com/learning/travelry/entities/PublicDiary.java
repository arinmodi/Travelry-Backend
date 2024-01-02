package com.learning.travelry.entities;

import java.math.BigInteger;
import java.sql.Timestamp;

public interface PublicDiary {

    BigInteger getId();

    String getName();

    String getCoverImage();

    String getHeaderImage();

    String getHeaderColor();

    Timestamp getCreatedDate();

    String getCreatorEmail();

    String getCreatorImage();

    String getCreatorUserName();

}
