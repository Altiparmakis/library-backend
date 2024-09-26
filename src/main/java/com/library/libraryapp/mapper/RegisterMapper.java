package com.library.libraryapp.mapper;

import com.library.libraryapp.dto.RegisterDTO;
import com.library.libraryapp.entity.Book;
import com.library.libraryapp.entity.CheckoutRegister;
import com.library.libraryapp.entity.Member;
import com.library.libraryapp.repository.BookRepository;
import com.library.libraryapp.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Component
public class RegisterMapper {

    private MemberRepository memberRepository;
    private BookRepository bookRepository;

    public RegisterDTO mapToRegisterDTO(CheckoutRegister checkoutRegister){
        RegisterDTO dto = new RegisterDTO();
        dto.setId(checkoutRegister.getId());
        dto.setMemberId(checkoutRegister.getMember().getId());
        dto.setBookId(checkoutRegister.getBook().getId());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        dto.setCheckoutDate(checkoutRegister.getCheckoutDate().format(formatter));
        dto.setDueDate(checkoutRegister.getDueDate().format(formatter));
        if(checkoutRegister.getReturnDate()!=null)dto.setReturnDate(checkoutRegister.getReturnDate().format(formatter));
        dto.setOverdueFine(checkoutRegister.getOverdueFine());
        return dto;

    }
    public CheckoutRegister mapToCheckoutRegistryEntity(RegisterDTO registerDTO){
        CheckoutRegister checkoutRegister = new CheckoutRegister();
        checkoutRegister.setId(registerDTO.getId());
        Member member = memberRepository.findById(registerDTO.getMemberId()).get();
        checkoutRegister.setMember(member);
        Book book = bookRepository.findById(registerDTO.getBookId()).get();
        checkoutRegister.setBook(book);
        checkoutRegister.setCheckoutDate(LocalDate.parse(registerDTO.getCheckoutDate()));
        if(registerDTO.getDueDate()!=null)
            checkoutRegister.setDueDate(LocalDate.parse(registerDTO.getDueDate()));
        if(registerDTO.getReturnDate()!=null)
            checkoutRegister.setReturnDate(LocalDate.parse(registerDTO.getReturnDate()));
        checkoutRegister.setOverdueFine(registerDTO.getOverdueFine());

        return checkoutRegister;
    }
}