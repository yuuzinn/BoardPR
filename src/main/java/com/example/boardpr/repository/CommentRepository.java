package com.example.boardpr.repository;

import com.example.boardpr.domain.Board;
import com.example.boardpr.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByBoardId(Long id, Pageable pageable);
}
