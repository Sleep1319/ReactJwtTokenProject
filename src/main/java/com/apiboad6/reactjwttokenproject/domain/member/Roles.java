package com.apiboad6.reactjwttokenproject.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Getter
    @Column(name = "role_name")
    private String roleName;


    @OneToMany(mappedBy = "roles")
    private List<Member> members;

}
