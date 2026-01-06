package com.web.ecommerce.repository;

import com.web.ecommerce.model.Address;
import com.web.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Iterable<Long> user(User user);

    List<Address> findAddressesByUserUserId(Long userUserId);

    List<Address> findAllByUser(User user);

    Address findAddressesByAddressId(Long addressId);
}
