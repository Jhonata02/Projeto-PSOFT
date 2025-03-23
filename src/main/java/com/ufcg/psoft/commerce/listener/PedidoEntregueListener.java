package com.ufcg.psoft.commerce.listener;

import com.ufcg.psoft.commerce.event.PedidoEntregueEvent;

public interface PedidoEntregueListener {
    void notificaPedidoEntregue(PedidoEntregueEvent PedidoEntregueEvent);
}
