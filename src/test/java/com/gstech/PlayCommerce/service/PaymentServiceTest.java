package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.exception.ResourceNotFoundException;
import com.gstech.PlayCommerce.model.*;
import com.gstech.PlayCommerce.model.enums.PayStatus;
import com.gstech.PlayCommerce.model.enums.PaymentType;
import com.gstech.PlayCommerce.repository.PaymentMethodRepository;
import com.gstech.PlayCommerce.repository.PaymentRepository;
import com.gstech.PlayCommerce.repository.PaymentStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private PaymentStatusRepository paymentStatusRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Buy buy;
    private Client client;
    private com.gstech.PlayCommerce.model.PaymentMethod paymentMethod;
    private com.gstech.PlayCommerce.model.PaymentStatus paymentStatus;
    private Payment payment;

    @BeforeEach
    void setUp() {
        // GIVEN
        client = new Client();
        client.setId(1L);
        client.setName("Test Client");
        client.setEmail("test@example.com");

        buy = new Buy();
        buy.setId(1L);
        buy.setClient(client);

        paymentMethod = new com.gstech.PlayCommerce.model.PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setPaymentType(PaymentType.PIX);

        paymentStatus = new com.gstech.PlayCommerce.model.PaymentStatus();
        paymentStatus.setId(1L);
        paymentStatus.setStatus(PayStatus.APROVADO);

        payment = new Payment();
        payment.setId(1L);
        payment.setBuy(buy);
        payment.setClient(client);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus(paymentStatus);
    }

    @Test
    void shouldCreatePayment_WhenValidBuyAndClientProvided() {
        // GIVEN
        PaymentRequest paymentRequest = new PaymentRequest(PaymentType.PIX, PayStatus.APROVADO);

        when(paymentRepository.findByBuyId(buy.getId())).thenReturn(Optional.empty());
        when(paymentMethodRepository.findByPaymentType(PaymentType.PIX)).thenReturn(Optional.of(paymentMethod));
        when(paymentStatusRepository.findByStatus(PayStatus.APROVADO)).thenReturn(Optional.of(paymentStatus));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // WHEN
        Payment result = paymentService.createPayment(buy, client, paymentRequest);

        // THEN
        assertNotNull(result);
        assertEquals(payment.getId(), result.getId());
        verify(paymentRepository, times(1)).findByBuyId(buy.getId());
        verify(paymentMethodRepository, times(1)).findByPaymentType(PaymentType.PIX);
        verify(paymentStatusRepository, times(1)).findByStatus(PayStatus.APROVADO);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void shouldCreatePaymentMethodAndStatus_WhenTheyDoNotExist() {
        // GIVEN
        PaymentRequest paymentRequest = new PaymentRequest(PaymentType.CARTAO_CREDITO,
                PayStatus.PENDENTE);

        com.gstech.PlayCommerce.model.PaymentMethod newPaymentMethod = new com.gstech.PlayCommerce.model.PaymentMethod();
        newPaymentMethod.setId(2L);
        newPaymentMethod.setPaymentType(PaymentType.CARTAO_CREDITO);

        com.gstech.PlayCommerce.model.PaymentStatus newPaymentStatus = new com.gstech.PlayCommerce.model.PaymentStatus();
        newPaymentStatus.setId(2L);
        newPaymentStatus.setStatus(PayStatus.PENDENTE);

        when(paymentRepository.findByBuyId(buy.getId())).thenReturn(Optional.empty());
        when(paymentMethodRepository.findByPaymentType(PaymentType.CARTAO_CREDITO)).thenReturn(Optional.empty());
        when(paymentMethodRepository.save(any(com.gstech.PlayCommerce.model.PaymentMethod.class))).thenReturn(newPaymentMethod);
        when(paymentStatusRepository.findByStatus(PayStatus.PENDENTE)).thenReturn(Optional.empty());
        when(paymentStatusRepository.save(any(com.gstech.PlayCommerce.model.PaymentStatus.class))).thenReturn(newPaymentStatus);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // WHEN
        Payment result = paymentService.createPayment(buy, client, paymentRequest);

        // THEN
        assertNotNull(result);
        verify(paymentMethodRepository, times(1)).save(any(com.gstech.PlayCommerce.model.PaymentMethod.class));
        verify(paymentStatusRepository, times(1)).save(any(com.gstech.PlayCommerce.model.PaymentStatus.class));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void shouldThrowIllegalStateException_WhenPaymentAlreadyExistsForBuy() {
        // GIVEN
        PaymentRequest paymentRequest = new PaymentRequest(PaymentType.PIX, PayStatus.APROVADO);

        when(paymentRepository.findByBuyId(buy.getId())).thenReturn(Optional.of(payment));

        // WHEN / THEN
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> paymentService.createPayment(buy, client, paymentRequest));

        assertTrue(exception.getMessage().contains("Já existe um pagamento para a compra com id " + buy.getId()));
        verify(paymentRepository, times(1)).findByBuyId(buy.getId());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void shouldFindPaymentByBuyId_WhenPaymentExists() {
        // GIVEN
        Long buyId = 1L;

        when(paymentRepository.findByBuyId(buyId)).thenReturn(Optional.of(payment));

        // WHEN
        Payment result = paymentService.findPaymentByBuyId(buyId);

        // THEN
        assertNotNull(result);
        assertEquals(payment.getId(), result.getId());
        verify(paymentRepository, times(1)).findByBuyId(buyId);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenPaymentNotFoundByBuyId() {
        // GIVEN
        Long buyId = 999L;

        when(paymentRepository.findByBuyId(buyId)).thenReturn(Optional.empty());

        // WHEN / THEN
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> paymentService.findPaymentByBuyId(buyId));

        assertTrue(exception.getMessage().contains("Pagamento não encontrado para a compra com id " + buyId));
        verify(paymentRepository, times(1)).findByBuyId(buyId);
    }
}
