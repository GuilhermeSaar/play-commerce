package com.gstech.PlayCommerce.model;

import com.gstech.PlayCommerce.model.enums.PaymentStatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_status_pagamento")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name= "status_pagamento", nullable = false, unique = true)
    private PaymentStatusType status;

    @OneToMany(mappedBy = "paymentStatus", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

}