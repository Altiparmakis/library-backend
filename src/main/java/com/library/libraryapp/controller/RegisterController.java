package com.library.libraryapp.controller;

import com.library.libraryapp.dto.RegisterDTO;
import com.library.libraryapp.service.RegisterService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/registers")
public class RegisterController {

    private RegisterService registerService;

    @PostMapping("createRegister")
    // URL : http://localhost:8080/api/registers/createRegister
    public ResponseEntity<RegisterDTO> createRegister(@RequestBody RegisterDTO registerDTO){
        RegisterDTO updateRegisterDTO = registerService.createRegister(registerDTO);
        return new ResponseEntity<>(updateRegisterDTO, HttpStatus.CREATED);
    }

    @GetMapping("listAll")
    // URL : http://localhost:8080/api/registers/listAll
    public ResponseEntity<List<RegisterDTO>> getAllRegisters(){
        List<RegisterDTO> registers = registerService.getAllRegisters();
        return new ResponseEntity<>(registers,HttpStatus.OK);
    }
    @GetMapping("{id}")
    // URL : http://localhost:8080/api/registers/1
    public ResponseEntity<RegisterDTO> getRegistersById(@PathVariable Long id){
        RegisterDTO register = registerService.getRegisterById(id);
        return new ResponseEntity<>(register,HttpStatus.OK);
    }

    @PatchMapping("updateRegister/{id}")
    // URL : http://localhost:8080/api/registers/updateRegister/1
    public ResponseEntity<RegisterDTO> updateRegister(@PathVariable Long id,@RequestBody RegisterDTO registerDTO){
        registerDTO.setId(id);
        RegisterDTO registerDTO1 = registerService.updateRegister(registerDTO);
        return new ResponseEntity<>(registerDTO1,HttpStatus.OK);
    }
    @DeleteMapping("deleteRegister/{id}")
    // URL : http://localhost:8080/api/registers/deleteRegister/1
    public ResponseEntity<String> deleteRegister(@PathVariable Long id){
        registerService.deleteRegister(id);
        return new ResponseEntity<>("Register succesfully deleted",HttpStatus.OK);
    }

    @GetMapping("member/{id}")
    // URL : http://localhost:8080/api/registers/member/1
    public ResponseEntity<List<RegisterDTO>> getRegistersByMember(@PathVariable Long id){
        List<RegisterDTO> registers = registerService.getRegisterByMemberId(id);
        return new ResponseEntity<>(registers,HttpStatus.OK);
    }
    @GetMapping("book/{id}")
    // URL : http://localhost:8080/api/registers/book/1
    public ResponseEntity<List<RegisterDTO>> getRegistersByBook(@PathVariable Long id){
        List<RegisterDTO> registers = registerService.getRegisterByBookId(id);
        return new ResponseEntity<>(registers,HttpStatus.OK);
    }
}
