package com.ufcg.psoft.commerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Fornecedor;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorResponseDTO {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;

    @JsonProperty("cnpj")
    @NotBlank(message = "Cnpj obrigatorio")
    private String cnpj;

    @JsonProperty("entregadores")
    private List<Long> entregadoresId;

    @JsonProperty("cafes")
    @JsonIgnoreProperties({"fornecedor"})
    private List<Long> cafesId;

    public FornecedorResponseDTO(Fornecedor fornecedor){
        this.id = fornecedor.getId();
        this.cnpj = fornecedor.getCnpj();
        this.nome = fornecedor.getNome();
        this.entregadoresId = fornecedor.getEntregadores().stream().map(entregador -> entregador.getId()).collect(Collectors.toList());
        this.cafesId = fornecedor.getCafes().stream().map(cafe -> cafe.getId()).collect(Collectors.toList());
    }
}
