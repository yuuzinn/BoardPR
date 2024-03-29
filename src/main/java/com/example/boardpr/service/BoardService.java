package com.example.boardpr.service;

import com.example.boardpr.domain.Board;
import com.example.boardpr.domain.Category;
import com.example.boardpr.domain.Comment;
import com.example.boardpr.domain.User;
import com.example.boardpr.exception.NotFoundException;
import com.example.boardpr.repository.BoardRepository;
import com.example.boardpr.repository.CategoryRepository;
import com.example.boardpr.repository.CommentRepository;
import com.example.boardpr.util.type.CategoryName;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;

    public Page<Board> getList(int page, String keyword) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Board> spec = search(keyword);
        return this.boardRepository.findAll(spec, pageRequest);
    }

    public Board getBoard(Long id) {
        Optional<Board> board = this.boardRepository.findById(id);
        if (board.isPresent()) {
            return board.get();
        } else {
            throw new NotFoundException("Board not found");
        }
    }

    public void create(String title, String content, User user, String categoryName) {
        CategoryName cnValueOf = CategoryName.valueOf(categoryName);
        List<Category> byCategoryName = categoryRepository.findByCategoryName(cnValueOf);
        Category category = byCategoryName.get(0);
        Board build = Board.builder()
                .title(title)
                .content(content)
                .createDate(LocalDateTime.now())
                .user(user)
                .category(category)
                .build();
        this.boardRepository.save(build);
    }

    public void modify(Board board, String title, String content, String categoryName) {
        CategoryName cnValueOf = CategoryName.valueOf(categoryName);
        List<Category> byCategoryName = categoryRepository.findByCategoryName(cnValueOf);
        Category category = byCategoryName.get(0);
        board.setTitle(title);
        board.setContent(content);
        board.setModifyDate(LocalDateTime.now());
        board.setCategory(category);
        this.boardRepository.save(board);
    }

    public void delete(Board board) {
        this.boardRepository.delete(board);
    }

    public void heart(Board board, User user) {
        board.getUserHeart().add(user);
        this.boardRepository.save(board);
    }

    private Specification<Board> search(String keyword) {
        return new Specification<Board>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Board> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                query.distinct(true);
                Join<Board, User> u1 = root.join("user", JoinType.LEFT);
                Join<Board, Comment> c = root.join("commentList", JoinType.LEFT);
                Join<Comment, User> u2 = root.join("user", JoinType.LEFT);
                return criteriaBuilder.or(criteriaBuilder.like(root.get("title"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("content"), "%" + keyword + "%"),
                criteriaBuilder.like(u1.get("username"), "%" + keyword + "%"),
                criteriaBuilder.like(c.get("content"), "%" + keyword + "%"),
                criteriaBuilder.like(u2.get("username"), "%" + keyword + "%"));
            }
        };
    }
}
