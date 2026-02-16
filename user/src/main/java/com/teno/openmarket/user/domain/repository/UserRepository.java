package com.teno.openmarket.user.domain.repository;

import com.teno.openmarket.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * [사용자 리포지토리]
 * <p>
 * User 애그리거트 루트(Aggregate Root)를 위한 도메인 리포지토리 인터페이스입니다.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}
