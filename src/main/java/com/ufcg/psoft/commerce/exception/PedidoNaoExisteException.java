package com.ufcg.psoft.commerce.exception;

public class PedidoNaoExisteException extends RuntimeException {
    public PedidoNaoExisteException() {
        super("O pedido consultado nao existe!");
    }
}
