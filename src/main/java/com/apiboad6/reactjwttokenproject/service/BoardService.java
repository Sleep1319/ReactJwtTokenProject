package com.apiboad6.reactjwttokenproject.service;

import com.apiboad6.reactjwttokenproject.domain.board.Board;
import com.apiboad6.reactjwttokenproject.domain.member.Member;
import com.apiboad6.reactjwttokenproject.dto.board.BoardResponse;
import com.apiboad6.reactjwttokenproject.dto.board.BoardUpdateRequest;
import com.apiboad6.reactjwttokenproject.dto.board.BoardWriteRequest;
import com.apiboad6.reactjwttokenproject.exception.NotFoundBoardException;
import com.apiboad6.reactjwttokenproject.repository.BoardRepository;
import com.apiboad6.reactjwttokenproject.repository.SignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final SignRepository signRepository;

    public List<BoardResponse> findBoard () {
        List<Board> boardList = boardRepository.findAll();
        return boardList.stream()
                .map(board -> new BoardResponse(
                        board.getId(),
                        board.getTitle(),
                        board.getContent(),
                        board.getMember().getId(),
                        board.getMember().getNickname() // 관계에서 닉네임 가져오기
                ))
                .collect(Collectors.toList());
    }

    public BoardResponse findByBoardWithMemberId(int id) {
        Board board = boardRepository.findById(id).orElseThrow(NotFoundBoardException::new);
        return new BoardResponse(board.getId(), board.getTitle(), board.getContent(),board.getMember().getId(), board.getMember().getNickname());
    }

    @Transactional
    public void writeBoard(BoardWriteRequest req) {
        Member member = signRepository.findById(req.getMemberId()).orElseThrow(NotFoundBoardException::new);
        boardRepository.save(BoardWriteRequest.toEntity(req, member));
    }

    @Transactional
    public void updateBoard(int id, BoardUpdateRequest req) {
        Board board = boardRepository.findById(id).orElseThrow(NotFoundBoardException::new);
        board.update(req.getTitle(), req.getContent());
    }

    @Transactional
    public void deleteBoard(int id) {
        Board board = boardRepository.findById(id).orElseThrow(NotFoundBoardException::new);
        boardRepository.delete(board);
    }
}
