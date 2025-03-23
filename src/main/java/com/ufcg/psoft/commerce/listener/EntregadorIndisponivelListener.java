package com.ufcg.psoft.commerce.listener;

import com.ufcg.psoft.commerce.event.EntregadorIndisponivelEvent;

public interface EntregadorIndisponivelListener {
    void notificaEntregadorIndisponivel(EntregadorIndisponivelEvent event);
}
