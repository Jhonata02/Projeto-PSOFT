package com.ufcg.psoft.commerce.services.pedido.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Pedido;
import jakarta.persistence.*;


public interface StateStatusDoPedido {

    void preparar();

    void finalizarPreparo();

    void enviarParaEntrega();

    void confirmarEntrega();
}
