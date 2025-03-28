package com.ufcg.psoft.commerce.services.pedido.strategy;

import com.ufcg.psoft.commerce.model.Enums.MetodoPagamento;
import com.ufcg.psoft.commerce.model.Pedido;

public class PagamentoPix implements PagamentoStrategy {

    @Override
    public void processaPagamento(Pedido pedido) {
        pedido.setMetodoPagamento(MetodoPagamento.PIX);
        pedido.setValorPedido(pedido.getValorPedido()*0.95);
    }
}
