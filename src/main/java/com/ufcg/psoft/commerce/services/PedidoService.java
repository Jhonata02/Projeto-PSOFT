package com.ufcg.psoft.commerce.services;

import com.ufcg.psoft.commerce.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.PedidoResponseDTO;

public interface PedidoService {

    PedidoResponseDTO criar(Long idCliente, String codigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);

    PedidoResponseDTO alterar(Long idPedido, Long idCliente, String codigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);

    PedidoResponseDTO recuperar(Long idPedido, Long idCliente, String codigoAcesso);

    void remover(Long idPedido, Long idCliente, String codigoAcesso);

}
