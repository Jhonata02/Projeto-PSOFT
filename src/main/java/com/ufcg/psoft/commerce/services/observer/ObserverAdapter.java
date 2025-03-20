package com.ufcg.psoft.commerce.services.observer;

import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.model.Pedido;

public abstract class ObserverAdapter implements Observer{
    @Override
    public void notificaCafeDisponivel(Cafe cafe) {
        // TODO Do nothing
    }

    @Override
    public void notificaPedidoEntregue(Pedido pedido) {
        // TODO Do nothing
    }

    @Override
    public void notificaNenhumEntregadorDisponivelParaEntrega(Pedido pedido) {
        // TODO Do nothing
    }

    @Override
    public void notificaPedidoSaiuParaEntrega(Pedido pedido) {
        // TODO Do nothing
    }
}
