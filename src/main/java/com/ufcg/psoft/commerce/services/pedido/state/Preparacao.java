package com.ufcg.psoft.commerce.services.pedido.state;

import com.ufcg.psoft.commerce.exception.CommerceException;
import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Pedido;

public class Preparacao implements StateStatusDoPedido{

    private final Pedido pedido;

    public Preparacao(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void preparar() {
        throw new CommerceException("pedido ja esta em preparo");
    }

    @Override
    public void finalizarPreparo() {
        pedido.setStatusPedidoState(new PedidoPronto(pedido));
        pedido.setStatusPedido(StatusPedido.PEDIDO_PRONTO);
    }

    @Override
    public void enviarParaEntrega() {
        throw new CommerceException("pedido ainda esta em preparo");
    }

    @Override
    public void confirmarEntrega() {
        throw new CommerceException("pedido ainda esta em preparo");
    }
}
