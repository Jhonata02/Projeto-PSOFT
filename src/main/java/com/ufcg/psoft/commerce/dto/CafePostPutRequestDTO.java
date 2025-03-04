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
@AllArgsConstructor
@NoArgsConstructor
public class CafePostPutRequestDTO {

    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;

    @JsonProperty("origem")
    @NotBlank(message = "Origem do cafe obrigatorio")
    private String origem;

    @JsonProperty("tipo")
    @Pattern(regexp = "grão|moído|cápsula", message = "O tipo do café tem que ser em grão, moído ou em cápsula")
    @NotBlank(message = "Tipo do cafe obrigatorio")
    private String tipo;

    @JsonProperty("perfilSensorial")
    @NotBlank(message = "Perfil sensorial do cafe obrigatorio")
    private String perfilSensorial;

    @JsonProperty("preco")
    @NotNull(message = "Preço do cafe obrigatorio")
    private double preco;

    @JsonProperty("isPremium")
    @NotNull(message = "Cafe normal ou premium")
    private boolean isPremium;

    @JsonProperty("tamanhoEmbalagem")
    @NotBlank(message = "Tamanho da embalagem do cafe obrigatorio")
    private String tamanhoEmbalagem;

}
