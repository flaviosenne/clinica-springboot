package com.curso.security.consulta.security.exception;

public class AccessDenidedException extends RuntimeException{
    public AccessDenidedException(String msg){
        super(msg);
    }
}
