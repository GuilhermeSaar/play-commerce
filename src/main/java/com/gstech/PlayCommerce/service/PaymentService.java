package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.PaymentRequestDTO;
import com.gstech.PlayCommerce.exception.*;
import com.gstech.PlayCommerce.model.*;
import com.gstech.PlayCommerce.repository.PaymentMethodRepository;
import com.gstech.PlayCommerce.repository.PaymentRepository;
import com.gstech.PlayCommerce.repository.PaymentStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentStatusRepository paymentStatusRepository;

    public PaymentService(PaymentRepository paymentRepository,
            PaymentMethodRepository paymentMethodRepository,
            PaymentStatusRepository paymentStatusRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentStatusRepository = paymentStatusRepository;
    }

    @Transactional
    public Payment createPayment(Buy buy, Client client, PaymentRequestDTO paymentRequest) {

        Optional<Payment> existingPayment = paymentRepository.findByBuyId(buy.getId());
        if (existingPayment.isPresent()) {
            throw new IllegalStateException("Já existe um pagamento para a compra com id " + buy.getId());
        }

        PaymentMethod paymentMethod = paymentMethodRepository
                .findByPaymentType(paymentRequest.paymentMethod())
                .orElseGet(() -> {
                    PaymentMethod newMethod = new PaymentMethod();
                    newMethod.setPaymentType(paymentRequest.paymentMethod());
                    return paymentMethodRepository.save(newMethod);
                });


        PaymentStatus paymentStatus = paymentStatusRepository
                .findByStatus(paymentRequest.paymentStatus())
                .orElseGet(() -> {
                    PaymentStatus newStatus = new PaymentStatus();
                    newStatus.setStatus(paymentRequest.paymentStatus());
                    return paymentStatusRepository.save(newStatus);
                });


        Payment payment = new Payment();
        payment.setBuy(buy);
        payment.setClient(client);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus(paymentStatus);
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public Payment findPaymentByBuyId(Long buyId) {
        return paymentRepository.findByBuyId(buyId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Pagamento não encontrado para a compra com id " + buyId));
    }
}
