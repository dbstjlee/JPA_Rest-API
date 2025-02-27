package com.tenco.blog_v2.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJPARepository extends JpaRepository<User, Integer> {
//
//    // 사용자 이름과 비밀번호를 조회하는 메서드 이름 기반 쿼리를 작성
//    Optional<User> findByUsernameAndPassword(@Param("username") String username,
//                                             @Param("password") String password);
//
//    // 사용자 이름
//    // 메서드 이름 기반
//    Optional<User> findByUsername(@Param("username") String username);

    // 사용자 이름과 비밀번호를 조회하는 메서드 이름 기반 쿼리를 작성
    Optional<User> findByUsernameAndPassword(String username, String password);

    // 사용자 이름으로 조회하는 메서드
    Optional<User> findByUsername(String username);

}
