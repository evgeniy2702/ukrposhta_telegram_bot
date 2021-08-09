package com.telegram_bot.Service;

import com.telegram_bot.model.Address;
import com.telegram_bot.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private AddressRepository addressRepository;

    @Autowired
    public void setAddressRepository(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address getById(Long id) {
        return addressRepository.getById(id);
    }

    public List<Address> getAllAddress(){
        return addressRepository.findAll();
    }
}
