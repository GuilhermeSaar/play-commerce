package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.BuyRequest;
import com.gstech.PlayCommerce.dto.PaymentResponse;
import com.gstech.PlayCommerce.exception.*;
import com.gstech.PlayCommerce.model.*;
import com.gstech.PlayCommerce.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /*
    * {
  "buyId": 42,
  "paymentMethod": "PIX",
  "paymentStatus": "PENDENTE"
}*/
    @Transactional
    public PaymentResponse createPayment(BuyRequest buyId, PaymentRequest data) {


    }

}
