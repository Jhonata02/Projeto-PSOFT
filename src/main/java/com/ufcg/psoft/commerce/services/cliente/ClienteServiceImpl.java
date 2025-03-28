package com.ufcg.psoft.commerce.services.cliente;

import com.ufcg.psoft.commerce.dto.CafeResponseDTO;
import com.ufcg.psoft.commerce.dto.PedidoResponseDTO;
import com.ufcg.psoft.commerce.exception.CafeNaoExisteException;
import com.ufcg.psoft.commerce.exception.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.CommerceException;
import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.repository.CafeRepository;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.dto.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.ClienteResponseDTO;
import com.ufcg.psoft.commerce.model.Cliente;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
        if (cafe.isDisponivel()) throw new CommerceException("Só pode demonstrar interesse em cafés indisponiveis");
        if (cafe.getClientesInteressados().contains(cliente)) throw new CommerceException("Você já demonstrou interesse nesse cafe");
        cafe.getClientesInteressados().add(cliente);
        cafeRepository.save(cafe);
        cliente.getCafesInteresse().add(cafe);
        clienteRepository.save(cliente);

    }

    private Pedido recuperarPedidoEspecificoDoCliente(Cliente cliente, Long idPedido) {
        if (!cliente.getPedidos().stream()
                .map(pedido -> pedido.getId())
                .collect(Collectors.toList())
                .contains(idPedido)) {
            throw new CommerceException("Esse pedido não pertence ao cliente");
        }
        Pedido pedido = cliente.getPedidos().stream().filter(pedido1 -> pedido1.getId().equals(idPedido)).findFirst().orElse(null);
        return  pedido;
    }

    @Override
    public PedidoResponseDTO exibirPedidoEspecifico(Long idPedido, Long idCliente, String codigoAcesso) {
        Cliente cliente = verificaClienteValido(idCliente,codigoAcesso);
        Pedido pedido = recuperarPedidoEspecificoDoCliente(cliente,idPedido);

        return new PedidoResponseDTO(pedido);
    }

    private List<PedidoResponseDTO> exibirHistoricoPedidos(Cliente cliente) {
        return cliente.getPedidos().stream()
                .sorted(Comparator
                        .comparing((Pedido pedido) -> pedido.getStatusPedido().toString().equals("PEDIDO ENTREGUE"))
                        .thenComparing(Pedido::getId, Comparator.reverseOrder()))
                .map(p -> new PedidoResponseDTO(p)).toList();
    }

    @Override
    public List<PedidoResponseDTO> exibirHistoricoPedidos(Long id, String codigoAcesso) {
        Cliente cliente = verificaClienteValido(id, codigoAcesso);
        return exibirHistoricoPedidos(cliente);
    }

    @Override
    public List<PedidoResponseDTO> exibirHistoricoPedidosComFiltro(Long id, String codigoAcesso, StatusPedido statusPedido) {
        Cliente cliente = verificaClienteValido(id, codigoAcesso);
        return cliente.getPedidos().stream()
                .filter(pedido -> pedido.getStatusPedido().toString().equals(statusPedido.toString())) // Filtra pelo status desejado
                .sorted(Comparator.comparing(Pedido::getId, Comparator.reverseOrder()))// Ordena do mais recente para o mais antigo
                .map(pedido -> new PedidoResponseDTO(pedido))
                .toList();
    }
}
