package com.web.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="addresses")
public class Address {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long addressId;
    @Column(nullable = false, length = 50)
    String country;
    @Column(nullable = false, length = 50)
    String city;
    @Column(nullable = false, length = 50)
    String street;
    @Column(nullable = false, length = 50)
    String zipcode;
    @Column(length = 50)
    String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



}
