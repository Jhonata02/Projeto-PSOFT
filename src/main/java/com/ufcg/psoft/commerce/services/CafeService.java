package com.ufcg.psoft.commerce.services;

import com.ufcg.psoft.commerce.dto.CafePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.CafeResponseDTO;

import java.util.List;

public interface CafeService {

    CafeResponseDTO criar(Long idFornecedor, String codigoAcesso, CafePostPutRequestDTO cafePostPutRequestDTO);

    CafeResponseDTO alterar(Long idFornecedor, Long idCafe, String codigoAcesso, CafePostPutRequestDTO cafePostPutRequestDTO);

    List<CafeResponseDTO> listar();

    CafeResponseDTO recuperar(Long id);

    void remover(Long id, Long idFornecedor, String codigoAcesso);

    List<CafeResponseDTO> listarPorNome(String nome);
}
