package com.ufcg.psoft.commerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Pedido;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class EntregadorResponseDTO {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;

    @JsonProperty("placa")
    @NotBlank(message = "Placa do veiculo obrigatorio")
    private String placa;

    @JsonProperty("tipoVeiculo")
    @NotBlank(message = "Tipo do veiculo obrigatorio")
    private String tipoVeiculo;

    @JsonProperty("corDoVeiculo")
    @NotBlank(message = "Cor do veiculo obrigatorio")
    private String corDoVeiculo;

    //@JsonProperty("pedidos")
    //private List<Long> pedidosId;

    public EntregadorResponseDTO(Entregador entregador) {
        this.id = entregador.getId();
        this.nome = entregador.getNome();
        this.placa = entregador.getPlaca();
        this.tipoVeiculo = entregador.getTipoVeiculo();
        this.corDoVeiculo = entregador.getCorDoVeiculo();
        //this.pedidosId = entregador.getPedidos().stream().map(pedido -> pedido.getId()).collect(Collectors.toList());
    }
}
