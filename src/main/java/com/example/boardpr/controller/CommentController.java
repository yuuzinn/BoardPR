package com.example.boardpr.controller;

import com.example.boardpr.controller.dto.CommentForm;
import com.example.boardpr.domain.Board;
import com.example.boardpr.service.BoardService;
import com.example.boardpr.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/comment")
@Controller
@RequiredArgsConstructor
public class CommentController {
    private final BoardService boardService;
    private final CommentService commentService;

    @PostMapping("/create/{id}")
    public String createComment(
            Model model,
            @PathVariable("id") Long id,
            @Valid CommentForm commentForm, BindingResult bindingResult) {
        Board board = this.boardService.getBoard(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("board", board);
            return "board_detail";
        }
        this.commentService.create(board, commentForm.getContent());
        return String.format("redirect:/board/detail/%s", id);
    }
}
