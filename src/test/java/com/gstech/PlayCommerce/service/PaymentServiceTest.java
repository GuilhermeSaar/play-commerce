package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.PaymentRequestDTO;
import com.gstech.PlayCommerce.exception.ResourceNotFoundException;
import com.gstech.PlayCommerce.model.*;
import com.gstech.PlayCommerce.model.enums.PaymentStatusType;
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
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
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

        paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setPaymentType(PaymentType.PIX);

        paymentStatus = new PaymentStatus();
        paymentStatus.setId(1L);
        paymentStatus.setStatus(PaymentStatusType.APROVADO);

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
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(PaymentType.PIX, PaymentStatusType.APROVADO);

        when(paymentRepository.findByBuyId(buy.getId())).thenReturn(Optional.empty());
        when(paymentMethodRepository.findByPaymentType(PaymentType.PIX)).thenReturn(Optional.of(paymentMethod));
        when(paymentStatusRepository.findByStatus(PaymentStatusType.APROVADO)).thenReturn(Optional.of(paymentStatus));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // WHEN
        Payment result = paymentService.createPayment(buy, client, paymentRequest);

        // THEN
        assertNotNull(result);
        assertEquals(payment.getId(), result.getId());
        verify(paymentRepository, times(1)).findByBuyId(buy.getId());
        verify(paymentMethodRepository, times(1)).findByPaymentType(PaymentType.PIX);
        verify(paymentStatusRepository, times(1)).findByStatus(PaymentStatusType.APROVADO);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void shouldCreatePaymentMethodAndStatus_WhenTheyDoNotExist() {
        // GIVEN
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(PaymentType.CARTAO_CREDITO,
                PaymentStatusType.PENDENTE);

        PaymentMethod newPaymentMethod = new PaymentMethod();
        newPaymentMethod.setId(2L);
        newPaymentMethod.setPaymentType(PaymentType.CARTAO_CREDITO);

        PaymentStatus newPaymentStatus = new PaymentStatus();
        newPaymentStatus.setId(2L);
        newPaymentStatus.setStatus(PaymentStatusType.PENDENTE);

        when(paymentRepository.findByBuyId(buy.getId())).thenReturn(Optional.empty());
        when(paymentMethodRepository.findByPaymentType(PaymentType.CARTAO_CREDITO)).thenReturn(Optional.empty());
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(newPaymentMethod);
        when(paymentStatusRepository.findByStatus(PaymentStatusType.PENDENTE)).thenReturn(Optional.empty());
        when(paymentStatusRepository.save(any(PaymentStatus.class))).thenReturn(newPaymentStatus);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // WHEN
        Payment result = paymentService.createPayment(buy, client, paymentRequest);

        // THEN
        assertNotNull(result);
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
        verify(paymentStatusRepository, times(1)).save(any(PaymentStatus.class));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void shouldThrowIllegalStateException_WhenPaymentAlreadyExistsForBuy() {
        // GIVEN
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(PaymentType.PIX, PaymentStatusType.APROVADO);

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
