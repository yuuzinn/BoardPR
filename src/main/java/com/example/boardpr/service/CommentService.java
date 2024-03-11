package com.example.boardpr.service;

import com.example.boardpr.domain.Board;
import com.example.boardpr.domain.Comment;
import com.example.boardpr.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void create(Board board, String content) {
        Comment build = Comment.builder()
                .content(content)
                .createDate(LocalDateTime.now())
                .board(board)
                .build();
        this.commentRepository.save(build);
    }
}
