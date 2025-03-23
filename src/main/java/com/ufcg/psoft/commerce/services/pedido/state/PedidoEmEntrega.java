package com.ufcg.psoft.commerce.services.pedido.state;

import com.ufcg.psoft.commerce.exception.CommerceException;
import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Pedido;

public class PedidoEmEntrega implements StateStatusDoPedido{
    private final Pedido pedido;

    public PedidoEmEntrega(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void preparar() {
        throw new CommerceException("Pedido ja está em rota de Entrega");
    }

    @Override
    public void finalizarPreparo() {
        throw new CommerceException("Pedido ja está em rota de Entrega");
    }

    @Override
    public void enviarParaEntrega() {
        throw new CommerceException("Pedido ja está em rota de Entrega");
    }

    @Override
    public void confirmarEntrega() {
        pedido.setStatusPedidoState(new PedidoEntregue(pedido));
        pedido.setStatusPedido(StatusPedido.PEDIDO_ENTREGUE);
    }
}
