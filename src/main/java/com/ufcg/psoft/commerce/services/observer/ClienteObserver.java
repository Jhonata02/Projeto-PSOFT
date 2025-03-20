package com.ufcg.psoft.commerce.services.observer;

import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.model.Pedido;
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
    public void notificaNenhumEntregadorDisponivelParaEntrega(Pedido pedido) {
        System.out.println(
                "\nCliente: " + pedido.getCliente().getNome()
                        + " Id: " + pedido.getCliente().getId()
                        + "\nSeu pedido de numero: " + pedido.getId()
                        + ", não saiu para entrega, pois não tem entregadores disponiveis no momento."
        );
    }

    @Override
    public void notificaPedidoSaiuParaEntrega(Pedido pedido) {
        System.out.println(
                "\nCliente: " + pedido.getCliente().getNome()
                        + " Id: " + pedido.getCliente().getId()
                        + "\nSeu pedido de numero: " + pedido.getId()
                        + ", saiu para entrega."
                        + "\nDetalhes do entregador:"
                        + "\nNome: " + pedido.getEntregador().getNome()
                        + "\nVeiculo: " + pedido.getEntregador().getTipoVeiculo()
                        + "\nPlaca do veiculo: " + pedido.getEntregador().getPlaca()
                        + "\nCor do veiculo: " + pedido.getEntregador().getCorDoVeiculo()
        );
    }
}
