package com.ocado.exceptions;

public class TooManyConfigProductsException extends RuntimeException{
    public TooManyConfigProductsException(String message){
        super(message);
    }
}
