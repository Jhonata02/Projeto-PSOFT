package com.ufcg.psoft.commerce.services.observer;

import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.services.observer.events.EventNenhumEntregadorDisponivel;
import com.ufcg.psoft.commerce.services.observer.events.EventPedidoSaiuParaEntrega;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.Objects;

public class ClienteObserver extends ObserverAdapter {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClienteObserver that = (ClienteObserver) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public void notificaCafeDisponivel(Cafe cafe) {
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
    public void notificaNenhumEntregadorDisponivelParaEntrega(EventNenhumEntregadorDisponivel event) {
        System.out.println(
                "\nCliente: " + event.getNomeDoCliente()
                        + " Id: " + event.getIdCliente()
                        + "\nSeu pedido de numero: " + event.getIdPedido()
                        + ", não saiu para entrega, pois não tem entregadores disponiveis no momento."
        );
    }

    @Override
    public void notificaPedidoSaiuParaEntrega(EventPedidoSaiuParaEntrega event) {
        System.out.println(
                "\nCliente: " + event.getNomeCliente()
                        + " Id: " + event.getIdCliente()
                        + "\nSeu pedido de numero: " + event.getIdPedido()
                        + ", saiu para entrega."
                        + "\nDetalhes do entregador:"
                        + "\nNome: " + event.getNomeEntregador()
                        + "\nVeiculo: " + event.getTipoVeiculo()
                        + "\nPlaca do veiculo: " + event.getPlacaVeiculo()
                        + "\nCor do veiculo: " + event.getCorVeiculo()
        );
    }
}
