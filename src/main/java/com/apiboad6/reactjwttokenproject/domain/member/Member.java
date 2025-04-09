package com.apiboad6.reactjwttokenproject.domain.member;

import com.apiboad6.reactjwttokenproject.domain.board.Board;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/*
* 외부에서 참조되는 것을 막기 위해 직접 적인 사용은 디비와 연결하는 리포지토리 그후 서비스단에서 변환 필요
* */
@Entity
@Table(name = "member")
@NoArgsConstructor
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email", unique = true)
    @NotNull
    private String email;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "nickname", unique = true)
    @NotNull
    private String nickname;

    @ManyToOne
    @JoinColumn(name = "roles_id", referencedColumnName = "id")
    private Roles roles;

    @OneToMany(mappedBy = "member")
    private List<Board> board;

    public Member(String email, String password, String username, String nickname, Roles roles) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.roles = roles;
    }
}
