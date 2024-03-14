package com.example.boardpr.controller;

import com.example.boardpr.controller.dto.SignupRequest;
import com.example.boardpr.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signup(SignupRequest signupRequest) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid SignupRequest signupRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!signupRequest.getPassword1().equals(signupRequest.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 비밀번호가 일치하지 않습니다.");
            return "signup_form";
        }
        try {
            userService.create(
                    signupRequest.getUsername(),
                    signupRequest.getPassword1(),
                    signupRequest.getEmail()
            );
        } catch (DataIntegrityViolationException e) {
            log.error("회원 가입 중 오류 발생 : {}", e.getMessage());
            bindingResult.reject("signupFailed", "이미 등록된 사용자가 있습니다.");
            return "signup_form";
        } catch (Exception e) {
            log.error("회원 가입 중 오류 발생 : {}", e.getMessage());
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
}
