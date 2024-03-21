package com.example.boardpr.util.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryName {
    FREE("자유"),
    TIP("팁");
    private final String description;
}
