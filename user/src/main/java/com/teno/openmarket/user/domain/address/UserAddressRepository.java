package com.teno.openmarket.user.domain.address;

public interface UserAddressRepository {

    UserAddress save(UserAddress userAddress);

    long countByUserId(Long userId);
}
