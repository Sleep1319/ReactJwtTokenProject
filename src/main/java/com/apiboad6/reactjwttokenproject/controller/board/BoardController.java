package com.apiboad6.reactjwttokenproject.controller.board;

import com.apiboad6.reactjwttokenproject.dto.board.BoardResponse;
import com.apiboad6.reactjwttokenproject.dto.board.BoardUpdateRequest;
import com.apiboad6.reactjwttokenproject.dto.board.BoardWriteRequest;
import com.apiboad6.reactjwttokenproject.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/api/boards")
    public ResponseEntity<?> getAllBoard() {
        List<BoardResponse> res = boardService.findBoard();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/api/board/{id}")
    public ResponseEntity<?> getBoardByMemberId (@PathVariable int id) {
        BoardResponse res = boardService.findByBoardWithMemberId(id);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/api/write-board")
    public ResponseEntity<String> writeBoard(@Valid @RequestBody BoardWriteRequest req) {
        boardService.writeBoard(req);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/api/board/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable int id, @Valid @RequestBody BoardUpdateRequest req) {
        boardService.updateBoard(id, req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/board/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable int id) {
        boardService.deleteBoard(id);
        return ResponseEntity.ok().build();
    }

}
