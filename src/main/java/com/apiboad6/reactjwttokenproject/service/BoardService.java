package com.apiboad6.reactjwttokenproject.service;

import com.apiboad6.reactjwttokenproject.config.jwt.CustomUserDetails;
import com.apiboad6.reactjwttokenproject.domain.board.Board;
import com.apiboad6.reactjwttokenproject.domain.member.Member;
import com.apiboad6.reactjwttokenproject.dto.board.BoardResponse;
import com.apiboad6.reactjwttokenproject.dto.board.BoardUpdateRequest;
import com.apiboad6.reactjwttokenproject.dto.board.BoardWriteRequest;
import com.apiboad6.reactjwttokenproject.exception.ForbiddenActionException;
import com.apiboad6.reactjwttokenproject.exception.NotFoundBoardException;
import com.apiboad6.reactjwttokenproject.exception.NotLoginException;
import com.apiboad6.reactjwttokenproject.repository.BoardRepository;
import com.apiboad6.reactjwttokenproject.repository.SignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    //요청 하기전 미리 걸러주는 검증이기에 구분이 필요하다
    @Transactional
    public void deleteBoard(int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            throw new NotLoginException();
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        int loginMemberId = user.getId();
        String role = user.getRoleName();

        Board board = boardRepository.findById(id).orElseThrow(NotFoundBoardException::new);

        if (loginMemberId != board.getMember().getId() && !role.equals("ROLE_ADMIN")) {
            throw new ForbiddenActionException();
        }

        boardRepository.delete(board);
    }
}
