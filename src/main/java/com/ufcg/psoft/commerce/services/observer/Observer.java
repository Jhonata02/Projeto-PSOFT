package com.ufcg.psoft.commerce.services.observer;

import com.ufcg.psoft.commerce.model.Cafe;

public interface Observer {
    void notificaCafeDisponivel(Cafe cafe);
}
