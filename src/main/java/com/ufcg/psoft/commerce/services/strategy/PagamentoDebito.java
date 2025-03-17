package com.ufcg.psoft.commerce.services.strategy;

import com.ufcg.psoft.commerce.model.Enums.MetodoPagamento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.services.PagamentoStrategy;

public class PagamentoDebito implements PagamentoStrategy {

    @Override
    public void processaPagamento(Pedido pedido) {
        pedido.setMetodoPagamento(MetodoPagamento.DEBITO);
        pedido.setValorPedido(pedido.getValorPedido()*0.975);
    }
}
