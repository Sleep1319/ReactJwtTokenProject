package com.apiboad6.reactjwttokenproject.repository;

import com.apiboad6.reactjwttokenproject.domain.member.Member;
import com.apiboad6.reactjwttokenproject.dto.SignInQueryResult;
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

//    //로그인 요청
@Query("SELECT new com.apiboad6.reactjwttokenproject.dto.SignInQueryResult(m.id, m.email, m.username, m.nickname, m.password, r.roleName) " +
        "FROM Member m JOIN m.roles r WHERE m.email = :email")
Optional<SignInQueryResult> findUserWithPasswordAndRole(@Param("email") String email);
//    @Query(value = "SELECT m.id, m.email, m.username, m.nickname, r.role_name FROM member m JOIN roles r on m.roles_id = r.id WHERE m.email = :email", nativeQuery = true)
//    Optional<SignInResponse> signInQuery(@Param("email") String email);
//
//    //비밀번호 찾기
//    @Query("SELECT m.password FROM Member m WHERE m.email = :email")
//    Optional<String> findPasswordByEmail(@Param("email") String email);

    boolean existsByEmail (String email);

    boolean existsByNickname (String nickname);

}
