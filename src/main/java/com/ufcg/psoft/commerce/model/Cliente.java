package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.event.CafeDisponivelEvent;
import com.ufcg.psoft.commerce.event.EntregadorIndisponivelEvent;
import com.ufcg.psoft.commerce.event.PedidoEmRotaEvent;
import com.ufcg.psoft.commerce.listener.CafeDisponivelListener;
import com.ufcg.psoft.commerce.listener.EntregadorIndisponivelListener;
import com.ufcg.psoft.commerce.listener.PedidoEmRotaListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente implements CafeDisponivelListener, PedidoEmRotaListener, EntregadorIndisponivelListener {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    @JsonProperty("endereco")
    @Column(nullable = false)
    private String endereco;

    @JsonIgnore
    @Column(nullable = false)
    private String codigo;

    @JsonProperty("isPremium")
    @Column(nullable = false)
    private boolean isPremium;

    @JsonProperty("pedidos")
    @JsonIgnoreProperties("cliente")
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

    @JsonProperty("cafesInteresse")
    @JsonIgnoreProperties("clientesInteressados")
    @ManyToMany()
    private List<Cafe> cafesInteresse;

    @Override
    public void notificaCafeDisponivel(CafeDisponivelEvent event) {
        Cafe cafe = event.getCafe();
        cafe.getClientesInteressados().stream()
                .sorted((cliente1, cliente2) -> Boolean.compare(cliente2.isPremium(),cliente1.isPremium()))
                .forEach(cliente -> {
                    System.out.println(
                            "\nCliente: " + cliente.getNome() + " Id: " + cliente.getId()
                                    + "\n"
                                    + "O cafe: " + cafe.getNome() + " Id: " + cafe.getId()
                                    + "\nestá disponivel"
                    );
                });
    }

    @Override
    public void notificaEntregadorIndisponivel(EntregadorIndisponivelEvent event) {
        Pedido pedido = event.getPedido();
        System.out.println(
                "\nCliente: " + pedido.getCliente().getNome()
                        + " Id: " + pedido.getCliente().getId()
                        + "\nSeu pedido de numero: " + pedido.getId()
                        + ", não saiu para entrega, pois não tem entregadores disponiveis no momento."
        );
    }

    @Override
    public void notificaPedidoEmRota(PedidoEmRotaEvent pedidoEmRotaEvent) {
        Pedido pedido = pedidoEmRotaEvent.getPedido();
        Entregador entregador = pedidoEmRotaEvent.getEntregador();
        System.out.println(
                "\nCliente: " + pedido.getCliente().getNome()
                        + " Id: " + pedido.getCliente().getId()
                        + "\nSeu pedido de numero: " + pedido.getId()
                        + ", saiu para entrega."
                        + "\nDetalhes do entregador:"
                        + "\nNome: " + entregador.getNome()
                        + "\nVeiculo: " + entregador.getTipoVeiculo()
                        + "\nPlaca do veiculo: " + entregador.getPlaca()
                        + "\nCor do veiculo: " + entregador.getCorDoVeiculo()
        );
    }
}
