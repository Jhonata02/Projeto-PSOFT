package com.ufcg.psoft.commerce.services.pedido.state;

import com.ufcg.psoft.commerce.exception.CommerceException;
import com.ufcg.psoft.commerce.model.Pedido;

public class PedidoEntregue implements StateStatusDoPedido{
    private final Pedido pedido;

    public PedidoEntregue(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void preparar() {
        throw new CommerceException("pedido ja foi Entregue");
    }

    @Override
    public void finalizarPreparo() {
        throw new CommerceException("pedido ja foi Entregue");
    }

    @Override
    public void enviarParaEntrega() {
        throw new CommerceException("pedido ja foi Entregue");
    }

    @Override
    public void confirmarEntrega() {
        throw new CommerceException("pedido ja foi Entregue");
    }
}
