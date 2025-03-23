package com.ufcg.psoft.commerce.listener;

import com.ufcg.psoft.commerce.event.PedidoEmRotaEvent;

public interface PedidoEmRotaListener {
    void notificaPedidoEmRota(PedidoEmRotaEvent pedidoEmRotaEvent);
}
