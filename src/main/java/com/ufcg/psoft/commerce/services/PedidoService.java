package com.ufcg.psoft.commerce.services;

import com.ufcg.psoft.commerce.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.PedidoResponseDTO;
import com.ufcg.psoft.commerce.model.MetodoPagamento;

public interface PedidoService {

    PedidoResponseDTO criar(Long idCliente, String codigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);

    PedidoResponseDTO alterar(Long idPedido, Long idCliente, String codigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);

    PedidoResponseDTO recuperar(Long idPedido, Long idCliente, String codigoAcesso);

    //void remover(Long idPedido, Long idCliente, String codigoAcesso);

    void confirmarPagamento(Long idPedido, Long idCliente, String codigoAcesso, MetodoPagamento metodoPagamento);

    void alterarStatusPedidoParaPronto(Long id, Long idFornecedor, String codigoAcesso);

    void alterarStatusPedidoEmEntrega(Long id, Long idFornecedor, String codigoAcesso);

    void alterarStatusPedidoParaEntregue(Long id, Long idCliente, String codigoAcesso);

    void cancelarPedido(Long id, Long idCliente, String codigoAcesso);

}
