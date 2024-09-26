package com.library.libraryapp.service.impl;

import com.library.libraryapp.dto.RegisterDTO;
import com.library.libraryapp.entity.CheckoutRegister;
import com.library.libraryapp.exception.ResourceNotFoundException;
import com.library.libraryapp.mapper.RegisterMapper;
import com.library.libraryapp.repository.CheckoutRegisterRepository;
import com.library.libraryapp.service.RegisterService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    @Value("${library.loanPeriodInDays}")
    private int loanPeriodInDays;

    @Value("${library.overdueFineRate}")
    private double overdueFineRate;

    private final CheckoutRegisterRepository checkoutRegisterRepository;
    private final RegisterMapper registerMapper;
    @Override
    public RegisterDTO createRegister(RegisterDTO registerDTO) {
        CheckoutRegister checkoutRegister = registerMapper.mapToCheckoutRegistryEntity(registerDTO);
        LocalDate dueDate = calculateDueDate(checkoutRegister.getCheckoutDate());
        checkoutRegister.setDueDate(dueDate);
        checkoutRegister = checkoutRegisterRepository.save(checkoutRegister);
        return registerMapper.mapToRegisterDTO(checkoutRegister);
    }

    @Override
    public List<RegisterDTO> getAllRegisters() {
        List<CheckoutRegister> checkoutRegisters = checkoutRegisterRepository.findAll();
        return checkoutRegisters.stream().map(registerMapper::mapToRegisterDTO).collect(Collectors.toList());
    }

    @Override
    public RegisterDTO getRegisterById(Long id) {
        Optional<CheckoutRegister> checkoutRegisterOptional = checkoutRegisterRepository.findById(id);
        CheckoutRegister checkoutRegister = checkoutRegisterOptional.orElseThrow(
                () -> new ResourceNotFoundException("Checkout Register","ID",id)
        );
        return registerMapper.mapToRegisterDTO(checkoutRegister);
    }

    @Override
    public RegisterDTO updateRegister(RegisterDTO registerDTO) {
        Optional<CheckoutRegister> checkoutRegisterOptional = checkoutRegisterRepository.findById(registerDTO.getId());
        CheckoutRegister checkoutRegisterToUpdate = checkoutRegisterOptional.orElseThrow(
                () -> new ResourceNotFoundException("Checkout Register","ID",registerDTO.getId())
        );
        updateCheckoutRegisterFromDTO(checkoutRegisterToUpdate,registerDTO);
        calculateOverdueFine(checkoutRegisterToUpdate);
        CheckoutRegister updatedCheckoutRegister = checkoutRegisterRepository.save(checkoutRegisterToUpdate);
        return registerMapper.mapToRegisterDTO(updatedCheckoutRegister);
    }

    @Override
    public void deleteRegister(Long id) {
        if(checkoutRegisterRepository.existsById(id)){
            throw new ResourceNotFoundException("Checkout Register", "ID",id);
        }
        checkoutRegisterRepository.deleteById(id);
    }

    @Override
    public List<RegisterDTO> getRegisterByMemberId(Long memberId) {
        List<CheckoutRegister> checkoutRegisters = checkoutRegisterRepository.findByMemberId(memberId);
        return checkoutRegisters.stream().map(registerMapper::mapToRegisterDTO).collect(Collectors.toList());

    }

    @Override
    public List<RegisterDTO> getRegisterByBookId(Long bookId) {
        List<CheckoutRegister> checkoutRegisters = checkoutRegisterRepository.findByBookId(bookId);
        return checkoutRegisters.stream().map(registerMapper::mapToRegisterDTO).collect(Collectors.toList());
    }

    private void calculateOverdueFine(CheckoutRegister checkoutRegister) {
        if(checkoutRegister.getReturnDate()!=null && checkoutRegister.getReturnDate().isAfter(checkoutRegister.getDueDate())){
            long daysOverdue = ChronoUnit.DAYS.between(checkoutRegister.getDueDate(),checkoutRegister.getReturnDate());
            double overdueFine = daysOverdue * overdueFineRate;
            checkoutRegister.setOverdueFine(overdueFine);
        }

    }

    private void updateCheckoutRegisterFromDTO(CheckoutRegister checkoutRegisterToUpdate, RegisterDTO registerDTO) {
        if(registerDTO.getDueDate()!=null)
            checkoutRegisterToUpdate.setDueDate(LocalDate.parse(registerDTO.getDueDate()));
        if(registerDTO.getReturnDate()!=null)
            checkoutRegisterToUpdate.setReturnDate(LocalDate.parse(registerDTO.getReturnDate()));

    }


    private LocalDate calculateDueDate(LocalDate checkoutDate) {
        return checkoutDate.plusDays(loanPeriodInDays);
    }
}
