package com.ufcg.psoft.commerce.services;

import com.ufcg.psoft.commerce.model.Pedido;

public interface PagamentoStrategy {
    void processaPagamento(Pedido pedido);
}
