package com.example.boardpr.controller;

import com.example.boardpr.controller.dto.BoardForm;
import com.example.boardpr.controller.dto.CommentForm;
import com.example.boardpr.domain.Board;
import com.example.boardpr.repository.BoardRepository;
import com.example.boardpr.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value ="page", defaultValue = "0") int page) {
        Page<Board> paging = boardService.getList(page);
        model.addAttribute("paging", paging);
        return "board_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(
            Model model,
            @PathVariable("id") Long id,
            CommentForm commentForm) {
        Board board = boardService.getBoard(id);
        model.addAttribute("board", board);
        return "board_detail";
    }

    @GetMapping("/create")
    public String create(BoardForm boardForm) {
        return "board_form";
    }

    /**
     * BindingResult 매개변수는 항상 @Valid 매개변수 바로 뒤에 위치해야 한다.
     * 만약 두 매개변수의 위치가 정확하지 않다면 @Valid만 적용되어 입력값 검증 실패 시 400 오류가 발생한다.
     */
    @PostMapping("/create")
    public String creat(
            @Valid BoardForm boardForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "board_form";
        }
        this.boardService.create(boardForm.getTitle(), boardForm.getContent());
        return "redirect:/board/list";
    }
}
