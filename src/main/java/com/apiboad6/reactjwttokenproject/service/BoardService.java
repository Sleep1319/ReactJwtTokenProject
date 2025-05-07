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
import com.apiboad6.reactjwttokenproject.repository.BoardRepositoryCustom;
import com.apiboad6.reactjwttokenproject.repository.SignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
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
    private final BoardRepositoryCustom boardRepositoryCustom;
    private final SignRepository signRepository;

    public Page<BoardResponse> findBoard(int page) {

        PageRequest pageable = PageRequest.of(page, 2, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> boards = boardRepository.findAll(pageable);

        //진짜 설정한 개수만 나올까?
        System.out.println("페이지 번호: " + page);
        System.out.println("총 게시글 수: " + boards.getTotalElements());
        System.out.println("전체 페이지 수: " + boards.getTotalPages());
        System.out.println("현재 페이지의 게시글 수: " + boards.getContent().size());

        List<BoardResponse> boardResponseList = boards.stream()
                .map(board -> new BoardResponse(
                        board.getId(),
                        board.getTitle(),
                        board.getContent(),
                        board.getMember().getId(),
                        board.getMember().getNickname()
                ))
                .toList();
        return new PageImpl<>(boardResponseList, pageable, boards.getTotalElements());
//        List<Board> boardList = boardRepository.findAll();
//        return boardList.stream()
//                .map(board -> new BoardResponse(
//                        board.getId(),
//                        board.getTitle(),
//                        board.getContent(),
//                        board.getMember().getId(),
//                        board.getMember().getNickname() // 관계에서 닉네임 가져오기
//                ))
//                .collect(Collectors.toList());
    }

    public Page<BoardResponse> searchBoard(String title, String nickname, int page) {
        Pageable pageable = PageRequest.of(page, 10);  // 한 페이지에 10개씩 보이도록 설정
        Page<Board> boards = boardRepositoryCustom.search(title, nickname, pageable);

        System.out.println("페이지 번호: " + page);
        System.out.println("총 게시글 수: " + boards.getTotalElements());
        System.out.println("전체 페이지 수: " + boards.getTotalPages());
        System.out.println("현재 페이지의 게시글 수: " + boards.getContent().size());

        List<BoardResponse> boardResponseList = boards.stream()
                .map(board -> new BoardResponse(
                        board.getId(),
                        board.getTitle(),
                        board.getContent(),
                        board.getMember().getId(),
                        board.getMember().getNickname()
                ))
                .toList();
        return new PageImpl<>(boardResponseList, pageable, boards.getTotalElements());
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            throw new NotLoginException();
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        int loginMemberId = user.getId();
//        String role = user.getRoleName();

        Board board = boardRepository.findById(id).orElseThrow(NotFoundBoardException::new);

        if (loginMemberId != board.getMember().getId()) {
            throw new ForbiddenActionException();
        }
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

        if (loginMemberId != board.getMember().getId() && !role.equals("ADMIN")) {
            throw new ForbiddenActionException();
        }

        boardRepository.delete(board);
    }
}
