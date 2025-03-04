package com.ufcg.psoft.commerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoPostPutRequestDTO {

    @JsonProperty("cafes")
    private List<Long> cafesId;

    @JsonProperty("enderecoDeEntrega")
    private String enderecoDeEntrega;
}
