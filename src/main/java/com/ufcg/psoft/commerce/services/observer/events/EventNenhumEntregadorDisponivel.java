package com.ufcg.psoft.commerce.services.observer.events;

public class EventNenhumEntregadorDisponivel {

    private Long idCliente;
    private Long idPedido;
    private String nomeDoCliente;

    public EventNenhumEntregadorDisponivel(Long idPedido,Long idCliente, String nomeDoCliente) {
        this.idCliente = idCliente;
        this.idPedido = idPedido;
        this.nomeDoCliente = nomeDoCliente;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public String getNomeDoCliente() {
        return nomeDoCliente;
    }
}
