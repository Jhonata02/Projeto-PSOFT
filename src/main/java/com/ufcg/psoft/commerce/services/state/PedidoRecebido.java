package com.ufcg.psoft.commerce.services.state;

import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.services.Impl.PedidoServiceImpl;

public class PedidoRecebido implements StateStatusDoPedido{

    @Override
    public void alterarStatus(Pedido pedido, PedidoServiceImpl  pedidoService) {
        pedido.setStatusPedido(StatusPedido.PREPARACAO);
        pedidoService.setEstadoDoPedido(new Preparacao());
    }
}
