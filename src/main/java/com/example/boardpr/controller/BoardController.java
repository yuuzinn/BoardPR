package com.example.boardpr.controller;

import com.example.boardpr.controller.dto.BoardForm;
import com.example.boardpr.controller.dto.CommentForm;
import com.example.boardpr.domain.Board;
import com.example.boardpr.domain.Category;
import com.example.boardpr.domain.Comment;
import com.example.boardpr.domain.User;
import com.example.boardpr.service.BoardService;
import com.example.boardpr.service.CommentService;
import com.example.boardpr.service.UserService;
import com.example.boardpr.util.type.CategoryName;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;
    private final CommentService commentService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        Page<Board> paging = boardService.getList(page, keyword);
        model.addAttribute("paging", paging);
        model.addAttribute("keyword", keyword);
        return "board_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(
            Model model,
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "0") int page,
            CommentForm commentForm) {
        Board board = boardService.getBoard(id);
        Pageable pageable = PageRequest
                .of(page, 5, Sort
                        .by(Sort.Direction.DESC, "createDate"));
        Page<Comment> commentPage = commentService.getCommentsByBoardId(id, pageable);
        model.addAttribute("board", board);
        model.addAttribute("comments", commentPage);
        return "board_detail";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(BoardForm boardForm) {
        return "board_form";
    }

    /**
     * BindingResult 매개변수는 항상 @Valid 매개변수 바로 뒤에 위치해야 한다.
     * 만약 두 매개변수의 위치가 정확하지 않다면 @Valid만 적용되어 입력값 검증 실패 시 400 오류가 발생한다.
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String creat(
            @Valid BoardForm boardForm,
            BindingResult bindingResult,
            Principal principal) {
        System.out.println(boardForm);
        User user = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            return "board_form";
        }
        this.boardService.
                create(boardForm.getTitle(),
                        boardForm.getContent(),
                        user,
                        boardForm.getCategory());
        return "redirect:/board/list";
    }

    @ModelAttribute("categories")
    public CategoryName[] categoryNames() {
        return CategoryName.values();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String boardModify(
            BoardForm board,
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Board bd = this.boardService.getBoard(id);
        if (!bd.getUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        board.setTitle(bd.getTitle());
        board.setContent(bd.getContent());
        return "board_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String boardModify(@Valid BoardForm boardForm, BindingResult bindingResult,
                              Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "board_form";
        }
        Board question = this.boardService.getBoard(id);
        if (!question.getUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.boardService.modify(question, boardForm.getTitle(), boardForm.getContent());
        return String.format("redirect:/board/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String boardDelete(Principal principal, @PathVariable("id") Long id) {
        Board question = this.boardService.getBoard(id);
        if (!question.getUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.boardService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/heart/{id}")
    public String boardHeart(Principal principal, @PathVariable("id") Long id) {
        Board board = this.boardService.getBoard(id);
        User user = this.userService.getUser(principal.getName());
        this.boardService.heart(board, user);
        return String.format("redirect:/board/detail/%s", id);
    }
}
