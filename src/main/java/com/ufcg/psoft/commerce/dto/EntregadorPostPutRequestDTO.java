package com.ufcg.psoft.commerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregadorPostPutRequestDTO {

    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;

    @JsonProperty("placa")
    @NotBlank(message = "Placa do veiculo obrigatorio")
    private String placa;

    @JsonProperty("tipoVeiculo")
    @NotBlank(message = "Tipo do veiculo obrigatorio")
    @Pattern(regexp = "Moto|Carro", message = "O tipo do veiculo tem que ser moto ou carro.")
    private String tipoVeiculo;

    @JsonProperty("corDoVeiculo")
    @NotBlank(message = "Cor do veiculo obrigatorio")
    private String corDoVeiculo;

    @JsonProperty("codigo")
    @NotNull(message = "Codigo de acesso obrigatorio")
    @Pattern(regexp = "^\\d{6}$", message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    private String codigo;
}
