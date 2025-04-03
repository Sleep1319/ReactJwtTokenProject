package com.apiboad6.reactjwttokenproject.repository;

import com.apiboad6.reactjwttokenproject.domain.member.Member;
import com.apiboad6.reactjwttokenproject.dto.SignInResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignRepository extends JpaRepository<Member, Integer> {
    //이메일로 찾기
    public Optional<Member> findByEmail(String email);
    
    //넥네임으로 찾기
    public Optional<Member> findByNickname (String nickname);

    //로그인 요청
    @Query(value = "SELECT m.id, m.email, m.username, m.nickname, r.role_name FROM member m JOIN roles r on m.roles_id = r.id WHERE m.email = :email AND m.password = :password", nativeQuery = true)
    Optional<SignInResponse> signInQuery(@Param("email") String email, @Param("password") String password);

    boolean existsByEmail (String email);

    boolean existsByNickname (String nickname);

}
