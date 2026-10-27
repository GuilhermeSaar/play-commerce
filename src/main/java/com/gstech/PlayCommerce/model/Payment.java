package com.gstech.PlayCommerce.model;

import com.gstech.PlayCommerce.model.enums.PayStatus;
import com.gstech.PlayCommerce.model.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_pagamento")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payment {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_compra", nullable = false)
    private Buy buy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_forma_pagamento", nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_status_pagamento")
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private LocalDateTime paymentDate = LocalDateTime.now();

    public Payment(PaymentStatus statusPayment, PaymentMethod paymentMethod) {
        this.paymentStatus = statusPayment;
        this.paymentMethod = paymentMethod;
    }

    public Payment(PaymentMethod method, PaymentStatus status) {

        this.paymentMethod = method;
        this.paymentStatus = status;

    }
}
