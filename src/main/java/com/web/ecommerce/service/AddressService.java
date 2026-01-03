package com.web.ecommerce.service;

import com.web.ecommerce.dto.AddressDTO;

import java.util.List;

public interface AddressService {


    List<AddressDTO> getUserAddresses();

    AddressDTO createAddress(AddressDTO addressDTO);

    AddressDTO updateAddress(AddressDTO addressDTO, Long addressId);

    String deleteAddress(Long addressId);

    AddressDTO getAddressByAddressId(Long addressId);
}
