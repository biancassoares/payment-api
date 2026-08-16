package com.soares.payment_api.exception;

public class PaymentUpdateNotAllowedException extends RuntimeException{
    public PaymentUpdateNotAllowedException(String message) {
        super(message);
    }
}
