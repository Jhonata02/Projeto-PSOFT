package com.ufcg.psoft.commerce.services.observer;

import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.services.observer.events.EventNenhumEntregadorDisponivel;
import com.ufcg.psoft.commerce.services.observer.events.EventPedidoEntregue;
import com.ufcg.psoft.commerce.services.observer.events.EventPedidoSaiuParaEntrega;

public interface Observer {
    void notificaCafeDisponivel(Cafe cafe);
    void notificaPedidoEntregue(EventPedidoEntregue event);
    void notificaNenhumEntregadorDisponivelParaEntrega(EventNenhumEntregadorDisponivel event);
    void notificaPedidoSaiuParaEntrega(EventPedidoSaiuParaEntrega event);
}
