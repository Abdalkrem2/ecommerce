package com.web.ecommerce.controller;

import com.web.ecommerce.dto.AddressDTO;
import com.web.ecommerce.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/addresses")
public class AddressController {

    @Autowired
    AddressService addressService;


    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) {
        AddressDTO address= addressService.createAddress(addressDTO);
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<AddressDTO>> getUserAddresses() {
        List<AddressDTO> addressDTOS=addressService.getUserAddresses();
        return new ResponseEntity<>(addressDTOS, HttpStatus.OK);
    }

    @PutMapping("{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@RequestBody AddressDTO addressDTO, @PathVariable Long addressId) {
        addressDTO=addressService.updateAddress(addressDTO,addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @DeleteMapping("{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        String status=addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping("{addressId}")
    public ResponseEntity<AddressDTO> getAddressByAddressId(@PathVariable Long addressId) {
        AddressDTO addressDTO=addressService.getAddressByAddressId(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);


    }
}
