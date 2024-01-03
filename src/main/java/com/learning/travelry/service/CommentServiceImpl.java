package com.learning.travelry.service;

import com.learning.travelry.entities.Comment;
import com.learning.travelry.entities.PublicComment;
import com.learning.travelry.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public boolean save(Comment comment) {
        try {
            commentRepository.save(comment);
            return true;
        }catch(Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public List<PublicComment> getComments(BigInteger id) {
        return commentRepository.getComments(id);
    }
}
