package com.gstech.PlayCommerce.model;

import com.gstech.PlayCommerce.model.enums.PayStatus;
import com.gstech.PlayCommerce.model.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_pagamento")
@NoArgsConstructor
@Getter
@Setter
public class Payment {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_compra", nullable = false)
    private Buy buy;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private PaymentType paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false)
    private PayStatus paymentStatus;

    @Column(nullable = false)
    private LocalDateTime paymentDate = LocalDateTime.now();

    public Payment(PaymentType paymentMethod, PayStatus paymentStatus) {
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }
}
