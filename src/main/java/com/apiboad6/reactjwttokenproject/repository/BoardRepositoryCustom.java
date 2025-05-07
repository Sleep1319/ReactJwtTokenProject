package com.apiboad6.reactjwttokenproject.repository;

import com.apiboad6.reactjwttokenproject.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepositoryCustom {

    public Page<Board> search(String title, String nickname, Pageable pageable);
}
