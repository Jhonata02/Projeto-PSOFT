package com.ufcg.psoft.commerce.services.Impl;

import com.ufcg.psoft.commerce.dto.CafePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.CafeResponseDTO;
import com.ufcg.psoft.commerce.exception.CafeNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.FornecedorNaoExisteException;
import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.model.Fornecedor;
import com.ufcg.psoft.commerce.repository.CafeRepository;
import com.ufcg.psoft.commerce.repository.FornecedorRepository;
import com.ufcg.psoft.commerce.services.CafeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CafeServiceImpl implements CafeService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CafeRepository cafeRepository;
    @Autowired
    FornecedorRepository fornecedorRepository;

    @Override
    public CafeResponseDTO criar(Long idFornecedor, String codigoAcesso, CafePostPutRequestDTO cafePostPutRequestDTO) {
        Fornecedor fornecedor = fornecedorRepository.findById(idFornecedor).orElseThrow(FornecedorNaoExisteException::new);
        if (!fornecedor.getCodigo().equals(codigoAcesso)){
            throw new CodigoDeAcessoInvalidoException();
        }
        Cafe cafe = modelMapper.map(cafePostPutRequestDTO, Cafe.class);
        cafeRepository.save(cafe);
        fornecedor.getCafes().add(cafe);
        fornecedorRepository.save(fornecedor);
        cafe.setDisponivel(true);
        cafe.setFornecedor(fornecedor);
        cafeRepository.save(cafe);
        return modelMapper.map(cafe, CafeResponseDTO.class);
    }

    private void verificaFornecedorValido(Long id, String codigoAcesso) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(FornecedorNaoExisteException::new);
        if (!fornecedor.getCodigo().equals(codigoAcesso)){
            throw new CodigoDeAcessoInvalidoException();
        }
    }


    @Override
    public CafeResponseDTO alterar(Long idFornecedor, Long idCafe, String codigoAcesso, CafePostPutRequestDTO cafePostPutRequestDTO) {
        verificaFornecedorValido(idFornecedor,codigoAcesso);
        Cafe cafe = cafeRepository.findById(idCafe).orElseThrow(CafeNaoExisteException::new);
        modelMapper.map(cafePostPutRequestDTO, cafe);
        cafeRepository.save(cafe);
        return modelMapper.map(cafe, CafeResponseDTO.class);
    }

    @Override
    public List<CafeResponseDTO> listar() {
        List<Cafe> cafes = cafeRepository.findAll();
        return cafes.stream()
                .map(CafeResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CafeResponseDTO> listarPorNome(String nome) {
        List<Cafe> cafes = cafeRepository.findByNomeContaining(nome);
        return cafes.stream()
                .map(CafeResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public CafeResponseDTO recuperar(Long id) {
        Cafe cafe = cafeRepository.findById(id).orElseThrow(CafeNaoExisteException::new);
        return new CafeResponseDTO(cafe);
    }

    @Override
    public void remover(Long id, Long idFornecedor, String codigoAcesso) {
        Cafe cafe = cafeRepository.findById(id).orElseThrow(CafeNaoExisteException::new);
        verificaFornecedorValido(idFornecedor, codigoAcesso);
        Fornecedor fornecedor = cafe.getFornecedor();
        if (!fornecedor.getCodigo().equals(codigoAcesso) || !fornecedor.getId().equals(idFornecedor)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        fornecedor.getCafes().remove(cafe);
        fornecedorRepository.save(fornecedor);
        cafeRepository.delete(cafe);
    }
}
