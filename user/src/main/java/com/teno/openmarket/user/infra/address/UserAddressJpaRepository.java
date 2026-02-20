package com.teno.openmarket.user.infra.address;

import com.teno.openmarket.user.domain.address.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAddressJpaRepository extends JpaRepository<UserAddress, Long> {
    long countByUserId(Long userId);

    List<UserAddress> findAllByUserIdOrderByIsDefaultDescIdDesc(Long userId);

    Optional<UserAddress> findByIdAndUserId(Long id, Long userId);

    Optional<UserAddress> findByUserIdAndIsDefaultTrue(Long userId);
}
