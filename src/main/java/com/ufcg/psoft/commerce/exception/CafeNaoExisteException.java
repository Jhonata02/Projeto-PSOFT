package com.ufcg.psoft.commerce.exception;

public class CafeNaoExisteException extends RuntimeException {
    public CafeNaoExisteException() {
        super("O cafe consultado nao existe!");
    }
}
