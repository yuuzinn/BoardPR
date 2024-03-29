package com.example.boardpr.service;

import com.example.boardpr.domain.Board;
import com.example.boardpr.domain.Comment;
import com.example.boardpr.domain.User;
import com.example.boardpr.exception.NotFoundException;
import com.example.boardpr.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Page<Comment> getCommentsByBoardId(Long id, Pageable pageable) {
        return commentRepository.findByBoardId(id, pageable);
    }

    public Comment create(Board board, String content, User user) {
        Comment build = Comment.builder()
                .content(content)
                .createDate(LocalDateTime.now())
                .board(board)
                .user(user)
                .build();
        this.commentRepository.save(build);
        return build;
    }

    public Comment getComment(Long id) {
        Optional<Comment> answer = this.commentRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new NotFoundException("answer not found");
        }
    }

    public void modify(Comment comment, String content) {
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        this.commentRepository.save(comment);
    }

    public void delete(Comment answer) {
        this.commentRepository.delete(answer);
    }

    public void heart(Comment comment, User user) {
        comment.getUserHeart().add(user);
        this.commentRepository.save(comment);
    }
}
