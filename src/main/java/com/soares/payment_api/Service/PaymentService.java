package com.soares.payment_api.Service;

import com.soares.payment_api.dto.PaymentRequest;
import com.soares.payment_api.dto.PaymentResponse;
import com.soares.payment_api.entity.Payment;
import com.soares.payment_api.enums.PaymentStatus;
import com.soares.payment_api.repository.PaymentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public PaymentResponse save(PaymentRequest request){

            Payment payment = new Payment();
            LocalDateTime now = LocalDateTime.now();

                payment.setDescription(request.getDescription());
                payment.setAmount(request.getAmount());
                payment.setStatus(PaymentStatus.PENDING);
                payment.setCreatedAt(now);
                payment.setExpiresAt(now.plusMinutes(15));
                //  payment.setPaidAt(null);

                payment = repository.save(payment);

                PaymentResponse response = new PaymentResponse();
                response.setId(payment.getId());
                response.setDescription(payment.getDescription());
                response.setAmount(payment.getAmount());
                response.setStatus(payment.getStatus());
                response.setCreatedAt(payment.getCreatedAt());
                response.setExpiresAt(payment.getExpiresAt());
                response.setPaidAt(payment.getPaidAt());

                return response;
    }


    public List<PaymentResponse> findAll() {

        List<Payment> payments = repository.findAll();
        List<PaymentResponse> responses = new ArrayList<>();

        for (Payment payment : payments) {

            PaymentResponse response = new PaymentResponse();

            response.setId(payment.getId());
            response.setDescription(payment.getDescription());
            response.setAmount(payment.getAmount());
            response.setStatus(payment.getStatus());
            response.setCreatedAt(payment.getCreatedAt());
            response.setExpiresAt(payment.getExpiresAt());
            response.setPaidAt(payment.getPaidAt());

            responses.add(response);
        }

        return responses;
    }

    public PaymentResponse findById(long id) {

        Payment existingPayment = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Payment not found"
                ));

        PaymentResponse response = new PaymentResponse();

        response.setId(existingPayment.getId());
        response.setDescription(existingPayment.getDescription());
        response.setAmount(existingPayment.getAmount());
        response.setStatus(existingPayment.getStatus());
        response.setCreatedAt(existingPayment.getCreatedAt());
        response.setExpiresAt(existingPayment.getExpiresAt());
        response.setPaidAt(existingPayment.getPaidAt());

        return response;
    }

    @Transactional
    public PaymentResponse update(long id, PaymentRequest request) {

        Payment existingPayment = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Payment not found"
                ));

        if (existingPayment.getStatus() != PaymentStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only pending payments can be updated"
            );
        }

        existingPayment.setDescription(request.getDescription());
        existingPayment.setAmount(request.getAmount());

        existingPayment = repository.save(existingPayment);

        PaymentResponse response = new PaymentResponse();

        response.setId(existingPayment.getId());
        response.setDescription(existingPayment.getDescription());
        response.setAmount(existingPayment.getAmount());
        response.setStatus(existingPayment.getStatus());
        response.setCreatedAt(existingPayment.getCreatedAt());
        response.setExpiresAt(existingPayment.getExpiresAt());
        response.setPaidAt(existingPayment.getPaidAt());

        return response;
    }

    public void delete (Long id){
        Payment existingPayment = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Payment not found"
                ));
        repository.delete(existingPayment);

    }
}
