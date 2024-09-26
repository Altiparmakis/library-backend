package com.library.libraryapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name="members")
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="postal_address_id")
    private PostalAddress postalAddress;

    private String email;

    private String phone;

    @Column(nullable = false)
    private LocalDate memberShipStarted;

    private LocalDate memberShipEnded;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private String barcodeNumber;

}
