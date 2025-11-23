package com.gstech.PlayCommerce.controller;

import com.gstech.PlayCommerce.dto.PaymentResponseDTO;
import com.gstech.PlayCommerce.model.Payment;
import com.gstech.PlayCommerce.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/buy/{buyId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByBuyId(@PathVariable Long buyId) {
        Payment payment = paymentService.findPaymentByBuyId(buyId);
        return ResponseEntity.ok(new PaymentResponseDTO(payment));
    }
}
