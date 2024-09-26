package com.library.libraryapp.mapper;

import com.library.libraryapp.dto.AddressDTO;
import com.library.libraryapp.entity.PostalAddress;

public class AddressMapper {

    public static AddressDTO mapToAddressDTO(PostalAddress postalAddress){
        AddressDTO dto = new AddressDTO();
        dto.setId(postalAddress.getId());
        dto.setCountry(postalAddress.getCountry());
        dto.setAdditionalInfo(postalAddress.getAdditionalInfo());
        dto.setPlaceName(postalAddress.getPlaceName());
        dto.setStreetNumber(postalAddress.getStreetNumber());
        dto.setZipCode(postalAddress.getZipCode());
        dto.setStreetName(postalAddress.getStreetName());
        return dto;
    }

    public static PostalAddress mapToAddressEntity(AddressDTO dto){
        PostalAddress pst = new PostalAddress();
        pst.setId(dto.getId());
        pst.setCountry(dto.getCountry());
        pst.setStreetNumber(dto.getStreetNumber());
        pst.setStreetName(dto.getStreetName());
        pst.setPlaceName(dto.getPlaceName());
        pst.setAdditionalInfo(dto.getAdditionalInfo());
        pst.setZipCode(dto.getZipCode());
        return pst;
    }
}
