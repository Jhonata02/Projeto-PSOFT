package com.ufcg.psoft.commerce.services.Impl;

import com.ufcg.psoft.commerce.dto.CafeResponseDTO;
import com.ufcg.psoft.commerce.exception.CafeNaoExisteException;
import com.ufcg.psoft.commerce.exception.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.repository.CafeRepository;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.dto.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.ClienteResponseDTO;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.services.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    CafeRepository cafeRepository;
    @Autowired
    ModelMapper modelMapper;


    private Cliente verificaClienteValido(Long id, String codigoAcesso) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(ClienteNaoExisteException::new);
        if (!cliente.getCodigo().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        return cliente;
    }
    @Override
    public ClienteResponseDTO alterar(Long id, String codigoAcesso, ClientePostPutRequestDTO clientePostPutRequestDTO) {
        Cliente cliente = verificaClienteValido(id,codigoAcesso);
        modelMapper.map(clientePostPutRequestDTO, cliente);
        clienteRepository.save(cliente);
        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO criar(ClientePostPutRequestDTO clientePostPutRequestDTO) {
        Cliente cliente = modelMapper.map(clientePostPutRequestDTO, Cliente.class);
        clienteRepository.save(cliente);
        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    @Override
    public void alterarPlano(Long id, String codigoAcesso) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(ClienteNaoExisteException::new);
        if (!cliente.getCodigo().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }

        cliente.setPremium(cliente.isPremium() ? false : true);
        clienteRepository.save(cliente);
    }

    @Override
    public List<CafeResponseDTO> exibirCatalogo(Long id, String tipo, String origem, String perfilSensorial) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(ClienteNaoExisteException::new);
        List<Cafe> cafes = (cliente.isPremium()) ? cafeRepository.findAll() : (cafeRepository.findAll().stream().filter(cafe -> !cafe.isPremium()).toList());

        if (tipo != null && !tipo.isEmpty()) {
            cafes = cafes.stream().filter(cafe -> cafe.getTipo().toLowerCase().contains(tipo.toLowerCase())).collect(Collectors.toList());
        }

        if (origem != null && !origem.isEmpty()) {
            cafes = cafes.stream().filter(cafe -> cafe.getOrigem().toLowerCase().contains(origem.toLowerCase())).collect(Collectors.toList());
        }

        if (perfilSensorial != null && !perfilSensorial.isEmpty()) {
            cafes = cafes.stream().filter(cafe -> cafe.getPerfilSensorial().toLowerCase().contains(perfilSensorial.toLowerCase())).collect(Collectors.toList());
        }


        return cafes.stream().sorted((cafe, t1) -> Boolean.compare(t1.isDisponivel(), cafe.isDisponivel()))
                .map(CafeResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public void remover(Long id, String codigoAcesso) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(ClienteNaoExisteException::new);
        if (!cliente.getCodigo().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        clienteRepository.delete(cliente);
    }

    @Override
    public List<ClienteResponseDTO> listarPorNome(String nome) {
        List<Cliente> clientes = clienteRepository.findByNomeContaining(nome);
        return clientes.stream()
                .map(ClienteResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClienteResponseDTO> listar() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(ClienteResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteResponseDTO recuperar(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(ClienteNaoExisteException::new);
        return new ClienteResponseDTO(cliente);
    }

    @Override
    public void demonstrarInteresseEmCafe(Long id, Long idCafe, String codigoAcesso) {
        Cliente cliente = verificaClienteValido(id,codigoAcesso);
        Cafe cafe = cafeRepository.findById(idCafe).orElseThrow(CafeNaoExisteException::new);
        cafe.getClientesInteressados().add(cliente);
        cafeRepository.save(cafe);
        cliente.getCafesInteresse().add(cafe);
        clienteRepository.save(cliente);
    }
}
