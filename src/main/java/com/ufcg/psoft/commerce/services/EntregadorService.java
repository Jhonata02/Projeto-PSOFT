package com.ufcg.psoft.commerce.services;

import com.ufcg.psoft.commerce.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.EntregadorResponseDTO;

import java.util.List;

public interface EntregadorService {

    EntregadorResponseDTO alterar(Long id, String codigoAcesso, EntregadorPostPutRequestDTO entregadorPostPutRequestDTO);

    List<EntregadorResponseDTO> listar();

    EntregadorResponseDTO recuperar(Long id);

    EntregadorResponseDTO criar(EntregadorPostPutRequestDTO entregadorPostPutRequestDTO);

    void remover(Long id, String codigoAcesso);

    List<EntregadorResponseDTO> listarPorNome(String nome);
}
