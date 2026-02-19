package com.teno.openmarket.user.domain.user;

import com.teno.openmarket.test.support.BaseRepositoryTest;
import com.teno.openmarket.user.infra.user.UserRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@Import(UserRepositoryImpl.class)
public class UserRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("유저 삭제 시 Soft Delete가 적용되어 일반 조회에서 제외되고, DB에는 is_deleted=true 상태로 남아야 한다")
    void should_SoftDeleteUser_When_DeleteIsCalled() {
        // given
        User user = User.builder()
                .email("softdeleted@test.com")
                .password("password")
                .name("soft")
                .phone("010-1234-5678")
                .role(Role.ROLE_USER)
                .build();

        User savedUser = userRepository.save(user);

        // when
        userRepository.delete(savedUser);

        // 실제 DB에 쿼리를 전송하고 영속성 컨텍스트(1차 캐시)틀 비움
        entityManager.flush();
        entityManager.clear();

        // then

        // 1. 일반 JPA 메서드로 조회 시 검색되지 않아야 함 (@SQLRestriction 작동 검증)
        assertThat(userRepository.findById(savedUser.getId())).isEmpty();

        // 2. Native Query로 직접 조회 시 is_deleted 값이 1(true)이어야 함
        Integer isDeleted = jdbcTemplate.queryForObject(
                "SELECT is_deleted FROM users WHERE id = ?", Integer.class, savedUser.getId()
        );
        assertThat(isDeleted).isEqualTo(1);

        // 3. deleted_at 시간도 DB에 잘 기록되는지 확인
        String deletedAt = jdbcTemplate.queryForObject(
                "SELECT deleted_at FROM users WHERE id = ?", String.class, savedUser.getId()
        );
        assertThat(deletedAt).isNotNull();
    }
}
