package com.learning.travelry.service;

import com.learning.travelry.entities.Member;
import com.learning.travelry.entities.PublicUser;
import com.learning.travelry.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Override
    public boolean save(Member member) {
        Member exist = memberRepository.findByEntityId(member.getEntityId());

        if (exist == null) {
            memberRepository.save(member);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<PublicUser> getMembersByDiaryId(BigInteger diaryId) {
        return memberRepository.findUsersByDiaryId(diaryId);
    }

}
