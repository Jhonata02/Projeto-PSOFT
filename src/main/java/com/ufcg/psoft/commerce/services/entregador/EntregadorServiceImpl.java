package com.ufcg.psoft.commerce.services.entregador;

import com.ufcg.psoft.commerce.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.EntregadorResponseDTO;
import com.ufcg.psoft.commerce.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Enums.StatusEntregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class EntregadorServiceImpl implements EntregadorService {

    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    ModelMapper modelMapper;

    private Entregador verificaEntregadorValido(Long id, String codigoAcesso) {
        Entregador entregador = entregadorRepository.findById(id).orElseThrow(EntregadorNaoExisteException::new);
        if (!entregador.getCodigo().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        return entregador;
    }

    @Override
    public EntregadorResponseDTO alterar(Long id, String codigoAcesso, EntregadorPostPutRequestDTO entregadorPostPutRequestDTO) {
        Entregador entregador = verificaEntregadorValido(id, codigoAcesso);
        modelMapper.map(entregadorPostPutRequestDTO, entregador);
        entregadorRepository.save(entregador);
        return modelMapper.map(entregador, EntregadorResponseDTO.class);
    }

    @Override
    public EntregadorResponseDTO criar(EntregadorPostPutRequestDTO entregadorPostPutRequestDTO) {
        Entregador entregador = modelMapper.map(entregadorPostPutRequestDTO, Entregador.class);
        entregadorRepository.save(entregador);
        return modelMapper.map(entregador, EntregadorResponseDTO.class);
    }

    @Override
    public void remover(Long id, String codigoAcesso) {
        Entregador entregador = verificaEntregadorValido(id, codigoAcesso);
        entregadorRepository.delete(entregador);
    }

    @Override
    public List<EntregadorResponseDTO> listarPorNome(String nome) {
        List<Entregador> entregadores = entregadorRepository.findByNomeContaining(nome);
        return entregadores.stream()
                .map(EntregadorResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<EntregadorResponseDTO> listar() {
        List<Entregador> entregadores = entregadorRepository.findAll();
        return entregadores.stream()
                .map(EntregadorResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public EntregadorResponseDTO recuperar(Long id) {
        Entregador entregador = entregadorRepository.findById(id).orElseThrow(EntregadorNaoExisteException::new);
        return new EntregadorResponseDTO(entregador);
    }

    @Override
    public void alterarStatus(Long id, String codigoAcesso) {
        Entregador entregador = verificaEntregadorValido(id,codigoAcesso);

        entregador.setStatusEntregador(entregador.getStatusEntregador() == StatusEntregador.ATIVO ? StatusEntregador.DESCANSO : StatusEntregador.ATIVO);

        entregadorRepository.save(entregador);
    }
}
