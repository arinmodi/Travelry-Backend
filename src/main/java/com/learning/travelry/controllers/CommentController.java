package com.learning.travelry.controllers;

import com.learning.travelry.entities.*;
import com.learning.travelry.payload.request.CommentRequest;
import com.learning.travelry.payload.response.MessageResponse;
import com.learning.travelry.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserService userService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private boolean isMemberOrOwner(Diary diary, String email) {
        List<PublicUser> members = memberService.getMembersByDiaryId(diary.getId());

        if (diary.getCreator().getEmail().equals(email)) {
            return true;
        } else {
            for (PublicUser pu : members) {
                if (pu.getEmail().equals(email)) {
                    return true;
                }
            }
        }

        return false;
    }

    @PostMapping("/{mediaId}/comment")
    public ResponseEntity<?> saveComment(
            @PathVariable("mediaId") String mediaId,
            @Valid @RequestBody CommentRequest commentRequest
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        User sender = userService.getUser(email);
        Media media = mediaService.getMediaById(new BigInteger(mediaId));

        if (media == null || sender == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid Request"));
        }

        Diary diary = media.getDiary();

        if (!isMemberOrOwner(diary, email)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Unauthorized access"));
        }

        long currentTimestampMillis = Instant.now().toEpochMilli();
        Comment nc = new Comment(sender, media, commentRequest.getContent(), new Timestamp(currentTimestampMillis));
        if (commentService.save(nc)) {
            CommentForWebSocket cws = new CommentForWebSocket(
                    nc.getCommentId(), nc.getContent(), nc.getCreated(),
                    nc.getSender().getProfilePhoto(), nc.getSender().getUsername(),
                    nc.getSender().getEmail()
            );
            messagingTemplate.convertAndSend("/topic/comment/"+mediaId, cws);
            return ResponseEntity.ok().body(new MessageResponse("comment added"));
        } else {
            return ResponseEntity.internalServerError().body(new MessageResponse("Server Error"));
        }
    }

    @GetMapping("/{mediaId}/comment")
    public ResponseEntity<?> getComments(
            @PathVariable("mediaId") String mediaId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        User sender = userService.getUser(email);
        Media media = mediaService.getMediaById(new BigInteger(mediaId));

        if (media == null || sender == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid Request"));
        }

        Diary diary = media.getDiary();

        if (!isMemberOrOwner(diary, email)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Unauthorized access"));
        }


        try {
            List<PublicComment> comments = commentService.getComments(new BigInteger(mediaId));
            return ResponseEntity.ok().body(comments);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(new MessageResponse("Server Error"));
        }
    }

}
