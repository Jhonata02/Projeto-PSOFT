package com.ufcg.psoft.commerce.services;

import com.ufcg.psoft.commerce.dto.CafeResponseDTO;
import com.ufcg.psoft.commerce.dto.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.ClienteResponseDTO;
import com.ufcg.psoft.commerce.dto.PedidoResponseDTO;
import com.ufcg.psoft.commerce.model.Enums.StatusPedido;

import java.util.List;

public interface ClienteService {

    ClienteResponseDTO alterar(Long id, String codigoAcesso, ClientePostPutRequestDTO clientePostPutRequestDTO);

    List<ClienteResponseDTO> listar();

    List<CafeResponseDTO> exibirCatalogo(Long id, String tipo, String origem, String perfilSensorial);

    ClienteResponseDTO recuperar(Long id);

    ClienteResponseDTO criar(ClientePostPutRequestDTO clientePostPutRequestDTO);

    void remover(Long id, String codigoAcesso);

    List<ClienteResponseDTO> listarPorNome(String nome);

    void alterarPlano(Long id, String codigoAcesso);

    void demonstrarInteresseEmCafe(Long id, Long idCafe, String codigoAcesso);

    PedidoResponseDTO exibirPedidoEspecifico(Long idPedido, Long idCliente, String codigoAcesso);

    List<PedidoResponseDTO> exibirHistoricoPedidosComFiltro(Long id, String codigoAcesso, StatusPedido statusPedido);

    List<PedidoResponseDTO> exibirHistoricoPedidos(Long id, String codigoAcesso);
}
