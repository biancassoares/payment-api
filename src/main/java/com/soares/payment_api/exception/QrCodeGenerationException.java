package com.soares.payment_api.exception;

public class QrCodeGenerationException extends RuntimeException{
    public QrCodeGenerationException(String message) {
        super(message);
    }
}
