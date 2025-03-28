package com.ufcg.psoft.commerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Pedido;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
public class ClienteResponseDTO {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;

    @JsonProperty("endereco")
    @NotBlank(message = "Endereco obrigatorio")
    private String endereco;

    @JsonProperty("isPremium")
    @NotBlank(message = "Plano obrigatório")
    private boolean isPremium;

    @JsonProperty("pedidos")
    @JsonIgnoreProperties({"cliente"})
    private List<Long> pedidosId;

    @JsonProperty("cafesInteresse")
    @JsonIgnoreProperties("clientesInteressados")
    private List<Long> cafesInteresseId;

    public ClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.endereco = cliente.getEndereco();
        this.isPremium = cliente.isPremium();
        this.pedidosId = cliente.getPedidos().stream().map(pedido -> pedido.getId()).collect(Collectors.toList());
        this.cafesInteresseId = cliente.getCafesInteresse()
                .stream()
                .map(cafe -> cafe.getId())
                .collect(Collectors.toList());
    }
}