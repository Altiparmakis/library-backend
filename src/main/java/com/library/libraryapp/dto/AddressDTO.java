package com.library.libraryapp.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    private String streetName;
    private Integer streetNumber;
    private String zipCode;
    private String placeName;
    private String country;
    private String additionalInfo;

}
