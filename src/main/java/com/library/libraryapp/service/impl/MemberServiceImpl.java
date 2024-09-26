package com.library.libraryapp.service.impl;

import com.library.libraryapp.dto.AddressDTO;
import com.library.libraryapp.dto.MemberDTO;
import com.library.libraryapp.entity.Member;
import com.library.libraryapp.entity.PostalAddress;
import com.library.libraryapp.exception.ResourceNotFoundException;
import com.library.libraryapp.mapper.AddressMapper;
import com.library.libraryapp.mapper.MemberMapper;
import com.library.libraryapp.repository.AddressRepository;
import com.library.libraryapp.repository.MemberRepository;
import com.library.libraryapp.service.AddressService;
import com.library.libraryapp.service.MemberService;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private AddressRepository addressRepository;
    private MemberRepository memberRepository;
    private AddressServiceImpl addressService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public MemberDTO addMember(MemberDTO memberDTO) {
        PostalAddress postalAddress = new PostalAddress();
        AddressDTO addressDTO = memberDTO.getAddress();
        if(addressDTO!=null){
            postalAddress = AddressMapper.mapToAddressEntity(addressDTO);
            postalAddress = addressRepository.save(postalAddress);
        }
        Member member = MemberMapper.mapToMemberEntity(memberDTO);
        if(postalAddress!=null)
            member.setPostalAddress(postalAddress);

        member = memberRepository.save(member);

        return MemberMapper.mapToMemberDTO(member);
    }

    @Override
    public List<MemberDTO> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream().map(MemberMapper::mapToMemberDTO).collect(Collectors.toList());
    }

    @Override
    public MemberDTO getMemberById(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        Member member = optionalMember.orElseThrow(
                () -> new ResourceNotFoundException("Member","ID",id)
        );
        return MemberMapper.mapToMemberDTO(member);
    }

    @Override
    @Transactional
    public MemberDTO updateMember(MemberDTO memberDTO) {
        Optional<Member> optionalMember = memberRepository.findById(memberDTO.getId());
        Member memberToUpdate = optionalMember.orElseThrow(
                () -> new ResourceNotFoundException("Member","ID",memberDTO.getId())
        );
        updateMemberEntityFromDTO(memberToUpdate,memberDTO);
        memberToUpdate = memberRepository.save(memberToUpdate);
        return MemberMapper.mapToMemberDTO(memberToUpdate);
    }

    @Override
    public void deleteMember(Long id) {
        if(memberRepository.existsById(id)){
            throw new ResourceNotFoundException("Member", "ID",id);
        }
        memberRepository.deleteById(id);
    }

    @Override
    public List<MemberDTO> findMembersByCriteria(Long id, String firstName, String lastName, String barcodeNumber) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Member.class);
        Root<Member> memberRoot = cq.from(Member.class);
        List<Predicate> predicates = new ArrayList<>();

        if(id!=null)predicates.add(cb.equal(memberRoot.get("id"),id));
        if(firstName!=null && !firstName.isEmpty())predicates.add(cb.like(cb.lower(memberRoot.get("firstName")),"%"+firstName.toLowerCase() + "%"));
        if(lastName!=null && !lastName.isEmpty())predicates.add(cb.like(cb.lower(memberRoot.get("lastName")),"%"+lastName.toLowerCase() + "%"));
        if(barcodeNumber!=null && !barcodeNumber.isEmpty())predicates.add(cb.equal(cb.lower(memberRoot.get("barcodeNumber")),barcodeNumber.toLowerCase() ));
        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        List<Member> result = entityManager.createQuery(cq).getResultList();
        return result.stream().map(MemberMapper::mapToMemberDTO).collect(Collectors.toList());
    }

    private void updateMemberEntityFromDTO(Member memberToUpdate, MemberDTO memberDTO) {
        if(memberDTO.getFirstName()!=null)memberToUpdate.setFirstName(memberDTO.getFirstName());
        if(memberDTO.getLastName()!=null)memberToUpdate.setLastName(memberDTO.getLastName());
        if(memberDTO.getDateOfBirth()!=null)memberToUpdate.setDateOfBirth(LocalDate.parse(memberDTO.getDateOfBirth()));
        if(memberDTO.getEmail()!=null)memberToUpdate.setEmail(memberDTO.getEmail());
        if(memberDTO.getPhone()!=null)memberToUpdate.setPhone(memberDTO.getPhone());
        if(memberDTO.getBarcodeNumber()!=null)memberToUpdate.setBarcodeNumber(memberDTO.getBarcodeNumber());
        if(memberDTO.getMemberShipStarted()!=null)memberToUpdate.setMemberShipStarted(LocalDate.parse(memberDTO.getMemberShipStarted()));
        if(memberDTO.getMemberShipEnded()!=null) {
            if (memberDTO.getMemberShipEnded().isEmpty()) {
                memberToUpdate.setMemberShipEnded(null);
                memberToUpdate.setIsActive(true);
            } else {
                memberToUpdate.setMemberShipEnded(LocalDate.parse(memberDTO.getMemberShipEnded()));
                memberToUpdate.setIsActive(false);
            }
        }
        if(memberDTO.getAddress()!=null){
            PostalAddress addressToUpdate;
            if(memberToUpdate.getPostalAddress()!=null){
                addressToUpdate = memberToUpdate.getPostalAddress();
            }else{
                addressToUpdate = new PostalAddress();
            }
            addressService.updateAddressEntityFromDTO(addressToUpdate,memberDTO.getAddress());
            addressRepository.save(addressToUpdate);
            memberToUpdate.setPostalAddress(addressToUpdate);
        }

    }
}
