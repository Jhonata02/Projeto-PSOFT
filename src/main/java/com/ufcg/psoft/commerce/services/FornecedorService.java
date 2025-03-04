package com.ufcg.psoft.commerce.services;

import com.ufcg.psoft.commerce.dto.FornecedorPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.FornecedorResponseDTO;

public interface FornecedorService {
    FornecedorResponseDTO alterar(Long id, String codigoAcesso, FornecedorPostPutRequestDTO fornecedorPostPutRequestDTO);


    FornecedorResponseDTO recuperar(Long id);

    FornecedorResponseDTO criar(FornecedorPostPutRequestDTO fornecedorPostPutRequestDTO);

    void remover(Long id, String codigoAcesso);

    void aprovarEntregador(Long idFornecedor, Long idEntregador, String codigoAcesso);

    //String rejeitarEntregador(Long idFornecedor, Long idEntregador, String codigoAcesso);

}
