package com.ufcg.psoft.commerce.services.observer.events;

public class EventPedidoSaiuParaEntrega {

    private Long idCliente;
    private Long idPedido;
    private String nomeCliente;
    private String nomeEntregador;
    private String tipoVeiculo;
    private String placaVeiculo;
    private String corVeiculo;

    public EventPedidoSaiuParaEntrega(Long idCliente, Long idPedido, String nomeCliente, String nomeEntregador, String tipoVeiculo, String placaVeiculo, String corVeiculo) {
        this.idCliente = idCliente;
        this.idPedido = idPedido;
        this.nomeCliente = nomeCliente;
        this.nomeEntregador = nomeEntregador;
        this.tipoVeiculo = tipoVeiculo;
        this.placaVeiculo = placaVeiculo;
        this.corVeiculo = corVeiculo;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getNomeEntregador() {
        return nomeEntregador;
    }

    public String getTipoVeiculo() {
        return tipoVeiculo;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public String getCorVeiculo() {
        return corVeiculo;
    }
}
