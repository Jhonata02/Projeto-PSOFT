package com.ufcg.psoft.commerce.services.observer;

import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.model.Pedido;

public interface Observer {
    void notificaCafeDisponivel(Cafe cafe);
    void notificaPedidoEntregue(Pedido pedido);
    void notificaNenhumEntregadorDisponivelParaEntrega(Pedido pedido);
    void notificaPedidoSaiuParaEntrega(Pedido pedido);
}
