package com.example.boardpr.service;

import com.example.boardpr.domain.Board;
import com.example.boardpr.domain.User;
import com.example.boardpr.exception.NotFoundException;
import com.example.boardpr.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public Page<Board> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(sorts));
        return this.boardRepository.findAll(pageRequest);
    }

    public Board getBoard(Long id) {
        Optional<Board> board = this.boardRepository.findById(id);
        if (board.isPresent()) {
            return board.get();
        } else {
            throw new NotFoundException("Board not found");
        }
    }

    public void create(String title, String content, User user) {
        Board build = Board.builder()
                .title(title)
                .content(content)
                .createDate(LocalDateTime.now())
                .user(user)
                .build();
        this.boardRepository.save(build);
    }

    public void modify(Board board, String title, String content) {
        board.setTitle(title);
        board.setContent(content);
        board.setModifyDate(LocalDateTime.now());
        this.boardRepository.save(board);
    }

    public void delete(Board board) {
        this.boardRepository.delete(board);
    }

    public void heart(Board board, User user) {
        board.getUserHeart().add(user);
        this.boardRepository.save(board);
    }
}
