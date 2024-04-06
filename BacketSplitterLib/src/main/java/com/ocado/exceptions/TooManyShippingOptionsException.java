package com.ocado.exceptions;

public class TooManyShippingOptionsException extends RuntimeException{
    public TooManyShippingOptionsException(String message){
        super(message);
    }
}
