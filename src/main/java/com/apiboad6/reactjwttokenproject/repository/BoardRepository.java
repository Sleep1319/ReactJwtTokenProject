package com.apiboad6.reactjwttokenproject.repository;

import com.apiboad6.reactjwttokenproject.domain.board.Board;
import com.apiboad6.reactjwttokenproject.dto.board.BoardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

//    @Query("SELECT b.id, b.title, b.content, m.nickname FROM Board b JOIN b.member m")
//    List<Board> findAll();
    Optional<Board> findById(int id);

    Board deleteById(int id);

    Page<Board> findAll(Pageable pageable);
}
