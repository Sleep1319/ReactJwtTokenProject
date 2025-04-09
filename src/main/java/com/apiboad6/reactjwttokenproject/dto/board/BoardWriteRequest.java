package com.apiboad6.reactjwttokenproject.dto.board;


import com.apiboad6.reactjwttokenproject.domain.board.Board;
import com.apiboad6.reactjwttokenproject.domain.member.Member;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardWriteRequest {

    @NotNull
    private int memberId;

    @NotNull
    private String title;

    @NotNull
    private String content;

    public static Board toEntity(BoardWriteRequest req, Member member) {
        return new Board(req.title, req.content, member);
    }
}
