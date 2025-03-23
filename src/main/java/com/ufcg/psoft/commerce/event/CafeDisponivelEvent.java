package com.ufcg.psoft.commerce.event;

import com.ufcg.psoft.commerce.model.Cafe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CafeDisponivelEvent {
    private Cafe cafe;
}
