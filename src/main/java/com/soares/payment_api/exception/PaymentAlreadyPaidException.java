package com.soares.payment_api.exception;

public class PaymentAlreadyPaidException extends RuntimeException{
    public PaymentAlreadyPaidException(String message) {
        super(message);
    }
}
