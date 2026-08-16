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

    public PaymentResponse save(PaymentRequest request) {

        Payment payment = new Payment();
        LocalDateTime now = LocalDateTime.now();

        payment.setDescription(request.getDescription());
        payment.setAmount(request.getAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(now);
        payment.setExpiresAt(now.plusMinutes(15));
        //  payment.setPaidAt(null);

        payment = repository.save(payment);
        return toResponse(payment);
    }


    public List<PaymentResponse> findAll() {

        List<Payment> payments = repository.findAll();
        List<PaymentResponse> responses = new ArrayList<>();

        for (Payment payment : payments) {

            responses.add(toResponse(payment));
        }

        return responses;
    }

    public PaymentResponse findById(long id) {

        Payment existingPayment = findPaymentById(id);
        return toResponse(existingPayment);
    }

    @Transactional
    public PaymentResponse update(long id, PaymentRequest request) {

        Payment existingPayment = findPaymentById(id);

        if (existingPayment.getStatus() != PaymentStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only pending payments can be updated"
            );
        }

        existingPayment.setDescription(request.getDescription());
        existingPayment.setAmount(request.getAmount());

        existingPayment = repository.save(existingPayment);

        return toResponse(existingPayment);
    }

    public void delete(Long id) {
        Payment payment = findPaymentById(id);
        repository.delete(payment);
    }

   private Payment findPaymentById(Long id){

        return repository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Payment not found"
                        ));
   }

   private PaymentResponse toResponse(Payment payment){
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
    public PaymentResponse pay(Long id) {

        Payment payment = findPaymentById(id);

        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment is already paid"
            );
        }

        if (payment.getStatus() == PaymentStatus.CANCELED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment is already cancelled"
            );
        }

        if (payment.getStatus() == PaymentStatus.EXPIRED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment expired"
            );
        }

        if (LocalDateTime.now().isAfter(payment.getExpiresAt())) {

            payment.setStatus(PaymentStatus.EXPIRED);
            repository.save(payment);

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment expired"
            );
        }

        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());

        payment = repository.save(payment);

        return toResponse(payment);
    }

    public PaymentResponse cancel(Long id){
        Payment payment = findPaymentById(id);
        if (payment.getStatus() == PaymentStatus.CANCELED){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment is already cancelled"
            );
        }
        if (payment.getStatus() == PaymentStatus.PAID){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment is already paid"
            );
        }
        if (payment.getStatus() == PaymentStatus.EXPIRED){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment is already expired"
            );
        }
        if (LocalDateTime.now().isAfter(payment.getExpiresAt())) {
            payment.setStatus(PaymentStatus.EXPIRED);
            repository.save(payment);

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment is already expired"
            );
        }

        payment.setStatus(PaymentStatus.CANCELED);
        payment = repository.save(payment);
        return  toResponse(payment);
    }



}

