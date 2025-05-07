package com.apiboad6.reactjwttokenproject.repository;

import com.apiboad6.reactjwttokenproject.domain.member.Member;
import com.apiboad6.reactjwttokenproject.dto.sign.SignInQueryResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignRepository extends JpaRepository<Member, Integer> {
//    @Modifying 딜리트 업데이트시
    
    Optional<Member> findById (int id);
    //이메일로 찾기
    public Optional<Member> findByEmail(String email);
    
    //넥네임으로 찾기
    public Optional<Member> findByNickname (String nickname);

//    //로그인 요청
    //인티티 클레스인 member아 아니기에 DTO 생성자와 자동맵픙을 못하여 아래와 같이 호출(내가 요청하는게 nativeQuery기 때문에 JPQL로 변경
@Query("SELECT  new com.apiboad6.reactjwttokenproject.dto.sign.SignInQueryResult " +
        "(m.id, m.email, m.username, m.nickname, m.password, r.roleName) " +
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
