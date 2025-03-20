package com.ufcg.psoft.commerce.services.observer;

import com.ufcg.psoft.commerce.services.observer.events.EventPedidoEntregue;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.Objects;

public class FornecedorObserver extends ObserverAdapter{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FornecedorObserver that = (FornecedorObserver) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public void notificaPedidoEntregue(EventPedidoEntregue event) {
        System.out.println(
                "\nFornecedor: " + event.getNomeFornecedor()
                        + " Id: " + event.getIdFornecedor()
                        + "\nO pedido de numero: " + event.getIdPedido()
                        + ", foi entregue ao cliente!"
        );
    }
}
