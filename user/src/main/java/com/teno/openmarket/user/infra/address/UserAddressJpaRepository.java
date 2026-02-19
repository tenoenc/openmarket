package com.teno.openmarket.user.infra.address;

import com.teno.openmarket.user.domain.address.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressJpaRepository extends JpaRepository<UserAddress, Long> {
    long countByUserId(Long userId);
}
