package com.ufcg.psoft.commerce.services.pedido.strategy;

import com.ufcg.psoft.commerce.model.Pedido;

public interface PagamentoStrategy {
    void processaPagamento(Pedido pedido);
}
