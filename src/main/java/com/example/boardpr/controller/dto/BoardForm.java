package com.example.boardpr.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardForm {
    @NotEmpty(message = "제목은 필수로 입력해야 합니다.")
    @Size(max = 200)
    private String title;

    @NotEmpty(message = "내용은 필수로 입력해야 합니다.")
    @Size(min = 10)
    private String content;
}
