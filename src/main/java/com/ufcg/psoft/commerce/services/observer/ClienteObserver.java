package com.ufcg.psoft.commerce.services.observer;

import com.ufcg.psoft.commerce.model.Cafe;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.Objects;

public class ClienteObserver extends ObserverAdapter {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}
