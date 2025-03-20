package com.ufcg.psoft.commerce.services.observer.events;

public class EventPedidoEntregue {
    private String nomeFornecedor;
    private Long idPedido;
    private Long idFornecedor;

    public EventPedidoEntregue(Long idFornecedor, Long idPedido, String nomeFornecedor) {
        this.idFornecedor = idFornecedor;
        this.idPedido = idPedido;
        this.nomeFornecedor = nomeFornecedor;
    }

    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public Long getIdFornecedor() {
        return idFornecedor;
    }
}
