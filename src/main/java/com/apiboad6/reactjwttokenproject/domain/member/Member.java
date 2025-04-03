package com.apiboad6.reactjwttokenproject.domain.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@NoArgsConstructor
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

    public Member(String email, String password, String username, String nickname, Roles roles) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.roles = roles;
    }
}
