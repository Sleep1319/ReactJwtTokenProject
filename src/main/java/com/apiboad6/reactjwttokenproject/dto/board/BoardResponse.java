package com.apiboad6.reactjwttokenproject.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {
    private int id;
    private String title;
    private String content;
    private int memberId;
    private String nickname;
}
