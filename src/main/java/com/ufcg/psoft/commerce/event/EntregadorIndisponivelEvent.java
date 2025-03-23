package com.ufcg.psoft.commerce.event;

import com.ufcg.psoft.commerce.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EntregadorIndisponivelEvent {
    private Pedido pedido;
}
