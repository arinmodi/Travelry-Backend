package com.learning.travelry.service;

import com.learning.travelry.entities.Comment;
import com.learning.travelry.entities.PublicComment;

import java.math.BigInteger;
import java.util.List;

public interface CommentService {

    boolean save(Comment comment);

    List<PublicComment> getComments(BigInteger id);

}
