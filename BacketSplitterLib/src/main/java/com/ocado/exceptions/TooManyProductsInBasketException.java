package com.ocado.exceptions;

public class TooManyProductsInBasketException extends RuntimeException {
    public TooManyProductsInBasketException(String message) {
        super(message);
    }
}
