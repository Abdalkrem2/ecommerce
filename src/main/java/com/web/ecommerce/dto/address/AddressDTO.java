package com.web.ecommerce.dto.address;
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
