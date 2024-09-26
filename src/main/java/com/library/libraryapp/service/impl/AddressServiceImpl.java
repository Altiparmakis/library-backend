package com.library.libraryapp.service.impl;

import com.library.libraryapp.dto.AddressDTO;
import com.library.libraryapp.entity.PostalAddress;
import com.library.libraryapp.exception.ResourceNotFoundException;
import com.library.libraryapp.mapper.AddressMapper;
import com.library.libraryapp.repository.AddressRepository;
import com.library.libraryapp.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private AddressRepository addressRepository;
    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        PostalAddress postalAddress = AddressMapper.mapToAddressEntity(addressDTO);
        postalAddress = addressRepository.save(postalAddress);
        return AddressMapper.mapToAddressDTO(postalAddress);
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<PostalAddress> postalAddresses= addressRepository.findAll();
        return postalAddresses.stream().map(AddressMapper::mapToAddressDTO).collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddressById(@PathVariable Long id) {
        PostalAddress address = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Address","ID",id)
        );
        return AddressMapper.mapToAddressDTO(address);
    }

    @Override
    public AddressDTO updateAddress(AddressDTO addressDTO) {
        Optional<PostalAddress> optionalAddressDTO = addressRepository.findById(addressDTO.getId());
        PostalAddress addressToUpdate = optionalAddressDTO.orElseThrow(
                () -> new ResourceNotFoundException("Address","ID",addressDTO.getId())
        );
        updateAddressEntityFromDTO(addressToUpdate,addressDTO);
        PostalAddress updatedAddress = addressRepository.save(addressToUpdate);
        return AddressMapper.mapToAddressDTO(updatedAddress);
    }

    @Override
    public void deleteAddress(Long id) {
        if(addressRepository.existsById(id)){
            throw new ResourceNotFoundException("Postal Address", "ID",id);
        }
        addressRepository.deleteById(id);
    }

    public void updateAddressEntityFromDTO(PostalAddress addressToUpdate, AddressDTO addressDTO) {
        if(addressDTO.getStreetName()!=null ) addressToUpdate.setStreetName(addressDTO.getStreetName());
        if(addressDTO.getStreetNumber()!=null) addressToUpdate.setStreetNumber(addressDTO.getStreetNumber());
        if(addressDTO.getCountry()!=null) addressToUpdate.setCountry(addressDTO.getCountry());
        if(addressDTO.getPlaceName()!=null) addressToUpdate.setPlaceName(addressDTO.getPlaceName());
        if(addressDTO.getZipCode()!=null) addressToUpdate.setZipCode(addressDTO.getZipCode());
        if(addressDTO.getAdditionalInfo()!=null) addressToUpdate.setAdditionalInfo(addressDTO.getAdditionalInfo());

    }
}
