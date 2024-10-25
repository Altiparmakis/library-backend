package com.library.libraryapp.controller;

import com.library.libraryapp.dto.AddressDTO;
import com.library.libraryapp.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/addresses")
public class AddressController {

    private AddressService addressService;
    @PostMapping("createAddress")
    // URL : http://localhost:8080/api/addresses/createAddress
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO){
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }
//this is a comment
    @GetMapping("listAll")
    // URL : http://localhost:8080/api/addresses/listAll
    public ResponseEntity<List<AddressDTO>> getAllAdresses(){
        List<AddressDTO> allAddresses = addressService.getAllAddresses();
        return new ResponseEntity<>(allAddresses,HttpStatus.OK);
    }

    @GetMapping("{id}")
    // URL : http://localhost:8080/api/addresses/1
    public ResponseEntity<AddressDTO> getAdressById(@PathVariable Long id){
        AddressDTO addressDTO = addressService.getAddressById(id);
        return new ResponseEntity<>(addressDTO,HttpStatus.OK);
    }

    @PatchMapping("updateAddress/{id}")
    // URL : http://localhost:8080/api/addresses/updateAddress/1
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long id,@RequestBody AddressDTO addressDTO){
        addressDTO.setId(id);
        AddressDTO updateAddress = addressService.updateAddress(addressDTO);
        return new ResponseEntity<>(updateAddress,HttpStatus.OK);
    }
    @DeleteMapping("deleteAddress/{id}")
    // URL : http://localhost:8080/api/addresses/deleteAddress
    public ResponseEntity<String> deleteAddress(@PathVariable Long id){
        addressService.deleteAddress(id);
        return new ResponseEntity<>("address succesfully deleted",HttpStatus.OK);
    }
}
