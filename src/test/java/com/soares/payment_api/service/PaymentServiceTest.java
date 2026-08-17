package com.soares.payment_api.service;

import com.soares.payment_api.dto.PaymentRequest;
import com.soares.payment_api.dto.PaymentResponse;
import com.soares.payment_api.entity.Payment;
import com.soares.payment_api.enums.PaymentStatus;
import com.soares.payment_api.exception.PaymentCanceledException;
import com.soares.payment_api.exception.PaymentExpiredException;
import com.soares.payment_api.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.soares.payment_api.exception.PaymentAlreadyPaidException;
import com.soares.payment_api.exception.PaymentNotFoundException;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository repository;

    @Mock
    private QrCodeService qrCodeService;

    @InjectMocks
    private PaymentService service;

    @Test
    void save() throws Exception {

        PaymentRequest request = new PaymentRequest();
        request.setAmount(BigDecimal.valueOf(10));
        request.setDescription("Sapato");

        Payment savedPayment = new Payment();
        savedPayment.setId(1L);
        savedPayment.setAmount(BigDecimal.valueOf(10));
        savedPayment.setDescription("Sapato");
        savedPayment.setStatus(PaymentStatus.PENDING);

        when(repository.save(any(Payment.class)))
                .thenReturn(savedPayment);

        when(qrCodeService.generateQrCode(anyString()))
                .thenReturn("fake-qr-code");

        PaymentResponse response = service.save(request);

        assertEquals(1L, response.getId());
        assertEquals(BigDecimal.valueOf(10), response.getAmount());
        assertEquals("Sapato", response.getDescription());
        assertEquals(PaymentStatus.PENDING, response.getStatus());
        assertEquals("fake-qr-code", response.getQrCode());
    }
    @Test
    void pay() {

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setAmount(BigDecimal.valueOf(10));
        payment.setDescription("Sapato");
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setExpiresAt(LocalDateTime.now().plusMinutes(15));

        when(repository.findById(1L))
                .thenReturn(Optional.of(payment));

        when(repository.save(any(Payment.class)))
                .thenReturn(payment);

        PaymentResponse response = service.pay(1L);

        assertEquals(PaymentStatus.PAID, response.getStatus());
        assertNotNull(response.getPaidAt());

    }

    @Test
    void shouldNotPayAlreadyPaidPayment() {

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus(PaymentStatus.PAID);

        when(repository.findById(1L))
                .thenReturn(Optional.of(payment));

        assertThrows(
                PaymentAlreadyPaidException.class,
                () -> service.pay(1L)
        );
    }

    @Test
    void shouldNotPayCancelledPayment() {

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus(PaymentStatus.CANCELED);

        when(repository.findById(1L))
                .thenReturn(Optional.of(payment));

        assertThrows(
                PaymentCanceledException.class,
                () -> service.pay(1L)
        );
    }

    @Test
    void shouldNotPayExpiredPayment() {

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        when(repository.findById(1L))
                .thenReturn(Optional.of(payment));

        assertThrows(
                PaymentExpiredException.class,
                () -> service.pay(1L)
        );
    }

    @Test
    void cancelPendingPayment() {

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setExpiresAt(LocalDateTime.now().plusMinutes(15));

        when(repository.findById(1L))
                .thenReturn(Optional.of(payment));

        when(repository.save(any(Payment.class)))
                .thenReturn(payment);

        PaymentResponse response = service.cancel(1L);

        assertEquals(PaymentStatus.CANCELED, response.getStatus());
    }

    @Test
    void shouldNotCancelPaidPayment() {

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus(PaymentStatus.PAID);

        when(repository.findById(1L))
                .thenReturn(Optional.of(payment));

        assertThrows(
                PaymentAlreadyPaidException.class,
                () -> service.cancel(1L)
        );
    }

    @Test
    void shouldNotCancelCancelledPayment() {

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus(PaymentStatus.CANCELED);

        when(repository.findById(1L))
                .thenReturn(Optional.of(payment));

        assertThrows(
                PaymentCanceledException.class,
                () -> service.cancel(1L)
        );
    }
    @Test
    void shouldNotCancelExpiredPayment() {

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        when(repository.findById(1L))
                .thenReturn(Optional.of(payment));

        assertThrows(
                PaymentExpiredException.class,
                () -> service.cancel(1L)
        );
    }
    @Test
    void shouldThrowWhenPaymentNotFound() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                PaymentNotFoundException.class,
                () -> service.findById(1L)
        );
    }

}