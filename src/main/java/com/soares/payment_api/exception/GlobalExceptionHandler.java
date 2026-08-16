package com.soares.payment_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PaymentNotFoundException.class)
    public String handlePaymentNotFound(PaymentNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PaymentAlreadyPaidException.class)
    public String handlePaymentAlreadyPaid (PaymentAlreadyPaidException ex){
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PaymentCanceledException.class)
    public String handlePaymentCanceled (PaymentCanceledException ex){
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PaymentExpiredException.class)
    public String handlePaymentExpired (PaymentExpiredException ex){
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PaymentUpdateNotAllowedException.class)
    public String handlePaymentUpdateNotAllowed (PaymentUpdateNotAllowedException ex){
        return ex.getMessage();
    }



}


