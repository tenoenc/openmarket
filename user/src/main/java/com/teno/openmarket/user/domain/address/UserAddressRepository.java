package com.teno.openmarket.user.domain.address;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository {

    UserAddress save(UserAddress userAddress);

    long countByUserId(Long userId);

    List<UserAddress> findAllByUserIdOrderByIsDefaultDescIdDesc(Long userId);

    Optional<UserAddress> findByIdAndUserId(Long id, Long userId);

    void delete(UserAddress address);
}
