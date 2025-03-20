package com.ufcg.psoft.commerce.services.observer;

import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.services.observer.events.EventNenhumEntregadorDisponivel;
import com.ufcg.psoft.commerce.services.observer.events.EventPedidoEntregue;
import com.ufcg.psoft.commerce.services.observer.events.EventPedidoSaiuParaEntrega;

public abstract class ObserverAdapter implements Observer{
    @Override
    public void notificaCafeDisponivel(Cafe cafe) {
        // TODO Do nothing
    }

    @Override
    public void notificaPedidoEntregue(EventPedidoEntregue event) {
        // TODO Do nothing
    }

    @Override
    public void notificaNenhumEntregadorDisponivelParaEntrega(EventNenhumEntregadorDisponivel event) {
        // TODO Do nothing
    }

    @Override
    public void notificaPedidoSaiuParaEntrega(EventPedidoSaiuParaEntrega event) {
        // TODO Do nothing
    }
}
