package com.learning.travelry.service;

import com.learning.travelry.entities.Member;
import com.learning.travelry.entities.PublicUser;

import java.math.BigInteger;
import java.util.List;

public interface MemberService {

    boolean save(Member member);

    List<PublicUser> getMembersByDiaryId(BigInteger diaryId);

}
