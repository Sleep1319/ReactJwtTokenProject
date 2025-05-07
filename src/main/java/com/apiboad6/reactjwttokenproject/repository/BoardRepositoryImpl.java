package com.apiboad6.reactjwttokenproject.repository;

import com.apiboad6.reactjwttokenproject.domain.board.Board;
import com.apiboad6.reactjwttokenproject.domain.board.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<Board> search(String title, String nickname, Pageable pageable) {
        QBoard qBoard = QBoard.board;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(title != null && !title.isBlank()) {
            booleanBuilder.and(qBoard.title.contains(title));
        }
        if(nickname != null && !nickname.isBlank()) {
            booleanBuilder.and(qBoard.member.nickname.contains(nickname));
        }

        List<Board> content = queryFactory
                .selectFrom(qBoard)//보드 엔티티에서 조회
                .where(booleanBuilder)//위 조립한 조건 적용
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qBoard.id.desc())
                .fetch();

        Long total = queryFactory
                .select(qBoard.count())
                .from(qBoard)
                .where(booleanBuilder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    // ✨ 조건을 따로 함수로 분리함
//    private BooleanExpression titleContains(String title) {
//        return (title != null && !title.isBlank()) ? QBoard.board.title.contains(title) : null;
//    }
//
//    private BooleanExpression nicknameContains(String nickname) {
//        return (nickname != null && !nickname.isBlank()) ? QBoard.board.member.nickname.contains(nickname) : null;
//    }
}
