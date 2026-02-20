package com.teno.openmarket.user.infra.address;

import com.teno.openmarket.user.domain.address.UserAddress;
import com.teno.openmarket.user.domain.address.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserAddressRepositoryImpl implements UserAddressRepository {

    private final UserAddressJpaRepository jpaRepository;

    @Override
    public UserAddress save(UserAddress userAddress) {
        return jpaRepository.save(userAddress);
    }

    @Override
    public long countByUserId(Long userId) {
        return jpaRepository.countByUserId(userId);
    }

    @Override
    public List<UserAddress> findAllByUserIdOrderByIsDefaultDescIdDesc(Long userId) {
        return jpaRepository.findAllByUserIdOrderByIsDefaultDescIdDesc(userId);
    }

    @Override
    public Optional<UserAddress> findByIdAndUserId(Long id, Long userId) {
        return jpaRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public void delete(UserAddress address) {
        jpaRepository.delete(address);
    }
}
