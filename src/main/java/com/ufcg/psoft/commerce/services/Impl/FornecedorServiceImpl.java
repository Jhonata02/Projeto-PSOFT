package com.ufcg.psoft.commerce.services.Impl;

import com.ufcg.psoft.commerce.dto.FornecedorPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.FornecedorResponseDTO;
import com.ufcg.psoft.commerce.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.exception.FornecedorNaoExisteException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Fornecedor;
import com.ufcg.psoft.commerce.model.Enums.StatusEntregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.FornecedorRepository;
import com.ufcg.psoft.commerce.services.FornecedorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FornecedorServiceImpl implements FornecedorService {

    @Autowired
    FornecedorRepository fornecedorRepository;
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public FornecedorResponseDTO alterar(Long id, String codigoAcesso, FornecedorPostPutRequestDTO fornecedorPostPutRequestDTO){
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(FornecedorNaoExisteException::new);
        if (!fornecedor.getCodigo().equals(codigoAcesso)){
            throw new CodigoDeAcessoInvalidoException();
        }
        modelMapper.map(fornecedorPostPutRequestDTO, fornecedor);
        fornecedorRepository.save(fornecedor);
        return modelMapper.map(fornecedor, FornecedorResponseDTO.class);
    }

    @Override
    public FornecedorResponseDTO criar(FornecedorPostPutRequestDTO fornecedorPostPutRequestDTO){
        Fornecedor fornecedor = modelMapper.map(fornecedorPostPutRequestDTO, Fornecedor.class);
        fornecedorRepository.save(fornecedor);
        return modelMapper.map(fornecedor, FornecedorResponseDTO.class);
    }

    @Override
    public void remover(Long id, String codigoAcesso) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(FornecedorNaoExisteException::new);
        if (!fornecedor.getCodigo().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        fornecedorRepository.delete(fornecedor);
    }

    @Override
    public FornecedorResponseDTO recuperar(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(FornecedorNaoExisteException::new);
        return new FornecedorResponseDTO(fornecedor);
    }

    @Override
    public void aprovarEntregador(Long idFornecedor, Long idEntregador, String codigoAcesso) {
        Fornecedor fornecedor = fornecedorRepository.findById(idFornecedor).orElseThrow(FornecedorNaoExisteException::new);
        Entregador entregador = entregadorRepository.findById(idEntregador).orElseThrow(EntregadorNaoExisteException::new);
        if (!fornecedor.getCodigo().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        fornecedor.getEntregadores().add(entregador);
        fornecedorRepository.save(fornecedor);
        entregador.setStatusEntregador(StatusEntregador.DESCANSO);
        entregadorRepository.save(entregador);
    }

    /** public String rejeitarEntregador(Long idFornecedor, Long idEntregador, String codigoAcesso) {
        Fornecedor fornecedor = fornecedorRepository.findById(idFornecedor).orElseThrow(FornecedorNaoExisteException::new);
        Entregador entregador = entregadorRepository.findById(idEntregador).orElseThrow(EntregadorNaoExisteException::new);
        if (!fornecedor.getCodigo().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        return "Entregador Rejeitador";
    }
    */
}
