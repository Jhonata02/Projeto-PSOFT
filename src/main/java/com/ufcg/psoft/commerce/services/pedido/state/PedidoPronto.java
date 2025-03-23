package com.ufcg.psoft.commerce.services.pedido.state;

import com.ufcg.psoft.commerce.exception.CommerceException;
import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Pedido;

public class PedidoPronto implements StateStatusDoPedido{
    private final Pedido pedido;

    public PedidoPronto(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void preparar() {
        throw new CommerceException("pedido ja esta Pronto");
    }

    @Override
    public void finalizarPreparo() {
        throw new CommerceException("pedido ja esta Pronto");
    }

    @Override
    public void enviarParaEntrega() {
        pedido.setStatusPedidoState(new PedidoEmEntrega(pedido));
        pedido.setStatusPedido(StatusPedido.PEDIDO_EM_ENTREGA);
    }

    @Override
    public void confirmarEntrega() {
        throw new CommerceException("pedido Pronto ainda não foi atribuido a um entregador");
    }
}
