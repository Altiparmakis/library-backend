package com.library.libraryapp.mapper;

import com.library.libraryapp.dto.MemberDTO;
import com.library.libraryapp.entity.Member;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MemberMapper {

    public static MemberDTO mapToMemberDTO(Member member){
        MemberDTO dto = new MemberDTO();
        dto.setId(member.getId());
        dto.setEmail(member.getEmail());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());
        dto.setPhone(member.getPhone());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        if(member.getDateOfBirth()!=null)
            dto.setDateOfBirth(member.getDateOfBirth().format(formatter));
        if(member.getPostalAddress()!=null)
            dto.setAddress(AddressMapper.mapToAddressDTO(member.getPostalAddress()));
        dto.setBarcodeNumber(member.getBarcodeNumber());
        if(member.getMemberShipStarted()!=null)
            dto.setMemberShipStarted(member.getMemberShipStarted().format(formatter));
        if(member.getMemberShipEnded()!=null)
            dto.setMemberShipEnded(member.getMemberShipEnded().format(formatter));
        dto.setIsActive(member.getIsActive());
        return dto;

    }
    public static Member mapToMemberEntity(MemberDTO memberDTO){
        Member member = new Member();
        member.setId(memberDTO.getId());
        member.setFirstName(memberDTO.getFirstName());
        member.setLastName(memberDTO.getLastName());

        member.setDateOfBirth(LocalDate.parse(memberDTO.getDateOfBirth()));
        if(memberDTO.getAddress()!=null)
            member.setPostalAddress(AddressMapper.mapToAddressEntity(memberDTO.getAddress()));
        member.setEmail(memberDTO.getEmail());
        member.setPhone(memberDTO.getPhone());
        member.setMemberShipStarted(LocalDate.parse(memberDTO.getMemberShipStarted()));
        if(memberDTO.getMemberShipEnded()!=null )
            member.setMemberShipEnded(LocalDate.parse(memberDTO.getMemberShipEnded()));
        member.setIsActive(memberDTO.getIsActive());
        member.setBarcodeNumber(memberDTO.getBarcodeNumber());
        return member;
    }
}
