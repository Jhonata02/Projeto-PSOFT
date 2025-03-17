package com.ufcg.psoft.commerce.services.strategy;

import com.ufcg.psoft.commerce.model.Enums.MetodoPagamento;
import com.ufcg.psoft.commerce.model.Pedido;

public class PagamentoCredito implements PagamentoStrategy {

    @Override
    public void processaPagamento(Pedido pedido) {
        pedido.setMetodoPagamento(MetodoPagamento.CREDITO);
    }
}
