package com.ufcg.psoft.commerce.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Fornecedor;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CafeResponseDTO {

    @Id
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

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

    @JsonProperty("isDisponivel")
    @NotNull()
    private boolean isDisponivel;

    @JsonProperty("tamanhoEmbalagem")
    @NotBlank(message = "Tamanho da embalagem do cafe obrigatorio")
    private String tamanhoEmbalagem;

    @JsonProperty("fornecedor")
    @JsonIgnoreProperties({"cafes"})
    @NotNull()
    private Fornecedor fornecedor;

    @JsonProperty("clientesInteressados")
    @JsonIgnoreProperties("cafesInteresse")
    private List<Long> clientesInteressados;

    public CafeResponseDTO(Cafe cafe) {
        this.id = cafe.getId();
        this.nome = cafe.getNome();
        this.isPremium = cafe.isPremium();
        this.origem = cafe.getOrigem();
        this.isDisponivel = cafe.isDisponivel();
        this.perfilSensorial = cafe.getPerfilSensorial();
        this.preco = cafe.getPreco();
        this.tamanhoEmbalagem = cafe.getTamanhoEmbalagem();
        this.tipo = cafe.getTipo();
        this.fornecedor = cafe.getFornecedor();
        this.clientesInteressados = cafe.getClientesInteressados()
                .stream()
                .map(cliente -> cliente.getId())
                .collect(Collectors.toList());
    }
}
