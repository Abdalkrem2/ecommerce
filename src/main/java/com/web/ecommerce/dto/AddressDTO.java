package com.web.ecommerce.dto;
import lombok.Data;
@Data
public class AddressDTO {
    Long addressId;
    String country;
    String city;
    String street;
    String zipcode;
    String state;
}
