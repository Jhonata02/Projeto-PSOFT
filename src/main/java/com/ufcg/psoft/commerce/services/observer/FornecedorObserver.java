package com.ufcg.psoft.commerce.services.observer;

import com.ufcg.psoft.commerce.model.Pedido;
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
    public void notificaPedidoEntregue(Pedido pedido) {
        System.out.println(
                "\nFornecedor: " + pedido.getFornecedor().getNome()
                        + " Id: " + pedido.getFornecedor().getId()
                        + "\nO pedido de numero: " + pedido.getId()
                        + ", foi entregue ao cliente!"
        );
    }
}
