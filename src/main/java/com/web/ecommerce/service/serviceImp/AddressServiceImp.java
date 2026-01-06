package com.web.ecommerce.service.serviceImp;

import com.web.ecommerce.dto.address.AddressDTO;
import com.web.ecommerce.exceptions.APIException;
import com.web.ecommerce.model.Address;
import com.web.ecommerce.model.User;
import com.web.ecommerce.repository.AddressRepository;
import com.web.ecommerce.repository.UserRepository;
import com.web.ecommerce.security.jwt.JwtUtils;
import com.web.ecommerce.service.AddressService;
import com.web.ecommerce.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImp implements AddressService {
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthUtil authUtil;


    @Override
    public List<AddressDTO> getUserAddresses() {
User user= authUtil.loggedInUser();
if(user==null){
    throw new APIException("User not logged in");
}
List<Address> addresses=addressRepository.findAllByUser(user);

        return addresses.stream().map(address->modelMapper.map(address,AddressDTO.class)).collect(Collectors.toList());
    }


    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
       User user = authUtil.loggedInUser();
       if(user==null){
           throw new APIException("User not logged in");
       }
       Address address=new Address();

       address.setCity(addressDTO.getCity());
       address.setState(addressDTO.getState());
       address.setCountry(addressDTO.getCountry());
       address.setStreet(addressDTO.getStreet());
       address.setZipcode(addressDTO.getZipcode());
       user.getAddresses().add(address);
       address.setUser(user);
       addressRepository.save(address);

        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public AddressDTO updateAddress(AddressDTO addressDTO, Long addressId) {
        Address address=addressRepository.findAddressesByAddressId(addressId);
        if(address==null){
            throw new APIException("Address not found");
        }
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setStreet(addressDTO.getStreet());
        address.setZipcode(addressDTO.getZipcode());
        addressRepository.save(address);

        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address address=addressRepository.findAddressesByAddressId(addressId);
        if(address==null){
            throw new APIException("Address not found");
        }
        addressRepository.delete(address);

        return "Address successfully deleted";
    }

    @Override
    public AddressDTO getAddressByAddressId(Long addressId) {
        Address address=addressRepository.findAddressesByAddressId(addressId);
        if(address==null){
            throw new APIException("Address not found");
        }

        return modelMapper.map(address,AddressDTO.class);
    }
}
