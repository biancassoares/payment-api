package com.soares.payment_api.exception;

public class PaymentCanceledException extends RuntimeException{
    public PaymentCanceledException(String message) {
        super(message);
    }
}
