package com.ufcg.psoft.commerce.services.pedido.state;

import com.ufcg.psoft.commerce.exception.CommerceException;
import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Pedido;

public class PedidoRecebido implements StateStatusDoPedido{
    private final Pedido pedido;

    public PedidoRecebido(Pedido pedido){
        this.pedido = pedido;
    }

    @Override
    public void preparar() {
        pedido.setStatusPedidoState(new Preparacao(pedido));
        pedido.setStatusPedido(StatusPedido.PREPARACAO);
    }

    @Override
    public void finalizarPreparo() {
        throw new CommerceException("Pedido ainda não foi pago.");
    }

    @Override
    public void enviarParaEntrega() {
        throw new CommerceException("Pedido ainda não foi pago.");
    }

    @Override
    public void confirmarEntrega() {
        throw new CommerceException("Pedido ainda não foi pago.");
    }
}
