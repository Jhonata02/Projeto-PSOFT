package com.ufcg.psoft.commerce.services.Impl;

import com.ufcg.psoft.commerce.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.PedidoResponseDTO;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.model.Enums.MetodoPagamento;
import com.ufcg.psoft.commerce.model.Enums.StatusEntregador;
import com.ufcg.psoft.commerce.services.observer.ClienteObserver;
import com.ufcg.psoft.commerce.services.observer.FornecedorObserver;
import com.ufcg.psoft.commerce.services.observer.Observer;
import com.ufcg.psoft.commerce.services.state.PedidoRecebido;
import com.ufcg.psoft.commerce.services.strategy.PagamentoStrategy;
import com.ufcg.psoft.commerce.services.strategy.PagamentoCredito;
import com.ufcg.psoft.commerce.repository.*;
import com.ufcg.psoft.commerce.services.PedidoService;
import com.ufcg.psoft.commerce.services.strategy.PagamentoDebito;
import com.ufcg.psoft.commerce.services.strategy.PagamentoPix;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    CafeRepository cafeRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    ItemPedidoRepository itemPedidoRepository;
    @Autowired
    FornecedorRepository fornecedorRepository;
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    ModelMapper modelMapper;

    private List<Observer> fornecedoresObservers = new ArrayList<>();
    private List<Observer> clientesObserves = new ArrayList<>();

    private final Map<MetodoPagamento, PagamentoStrategy> mapPagamentoStrategy = Map.of(
            MetodoPagamento.CREDITO, new PagamentoCredito(),
            MetodoPagamento.PIX, new PagamentoPix(),
            MetodoPagamento.DEBITO, new PagamentoDebito()
    );

    private ItemPedido criarItem(Cafe cafe, Pedido pedido) {
        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setValorDoItem(cafe.getPreco());
        item.setCafe(cafe);
        itemPedidoRepository.save(item);

        return item;

    }

    private Pedido criarPedido(Cliente cliente, String endereco) {
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatusPedido(new PedidoRecebido());
        pedido.setEnderecoDeEntrega(endereco == null || endereco == ""  ? cliente.getEndereco() : endereco);
        pedido.setValorPedido(0.0);
        pedido.setItens(new ArrayList<>());
        pedidoRepository.save(pedido);

        return pedido;
    }

    private Cliente validarCliente(Long idCliente, String codigoAcesso) {
        Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(ClienteNaoExisteException::new);
        if (!cliente.getCodigo().equals(codigoAcesso)) throw new CodigoDeAcessoInvalidoException();

        return cliente;
    }

    private void validarCafePremium(Cafe cafe, Cliente cliente) {
        if (cafe.isPremium() && !cliente.isPremium()) {
            throw new RuntimeException("o cliente não pode adicionar cafés premium no pedido");
        }
    }

    private List<ItemPedido> criarItensPedido(List<Long> cafesId,Cliente cliente,Pedido pedido) {
        List<ItemPedido> itens = new ArrayList<>();

        for (Long cafeId : cafesId) {
            Cafe cafe = cafeRepository.findById(cafeId)
                    .orElseThrow(CafeNaoExisteException::new);

            validarCafePremium(cafe, cliente);

            ItemPedido item = criarItem(cafe, pedido);
            itens.add(item);

        }
        return itens;
    }

    private double calcularValorPedido(List<ItemPedido> itens) {
        return itens.stream()
                .mapToDouble(item -> item.getCafe().getPreco())
                .sum();
    }

    private Pedido verificaPedidoPertenceAoCliente(Long idPedido, Long idCliente, String codigoAcesso) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(PedidoNaoExisteException::new);
        if (!pedido.getCliente().getId().equals(idCliente) || !pedido.getCliente().getCodigo().equals(codigoAcesso)) {
            throw new CommerceException("Esse pedido não corresponde ao cliente informado");
        }
        return pedido;
    }

    @Override
    public PedidoResponseDTO criar(Long idCliente, String codigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        Cliente cliente = validarCliente(idCliente, codigoAcesso);
        String endereco = pedidoPostPutRequestDTO.getEnderecoDeEntrega();

        Pedido pedido = criarPedido(cliente,endereco);

        List<ItemPedido> itens = criarItensPedido(pedidoPostPutRequestDTO.getCafesId(),cliente,pedido);
        double valorPedido = calcularValorPedido(itens);

        pedido.setItens(itens);
        pedido.setValorPedido(valorPedido);
        pedidoRepository.save(pedido);

        return new PedidoResponseDTO(pedido);
    }

    @Override
    public PedidoResponseDTO recuperar(Long idPedido, Long idCliente, String codigoAcesso) {
        validarCliente(idCliente, codigoAcesso);
        Pedido pedido = verificaPedidoPertenceAoCliente(idPedido,idCliente,codigoAcesso);
        return new PedidoResponseDTO(pedido);
    }

    @Override
    public PedidoResponseDTO alterar(Long idPedido, Long idCliente, String codigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        Cliente cliente = validarCliente(idCliente,codigoAcesso);

        Pedido pedido = verificaPedidoPertenceAoCliente(idPedido,idCliente,codigoAcesso);

        String endereco = pedidoPostPutRequestDTO.getEnderecoDeEntrega();

        List<ItemPedido> itens = criarItensPedido(pedidoPostPutRequestDTO.getCafesId(),cliente,pedido);

        double valorPedido = calcularValorPedido(itens);

        pedido.setEnderecoDeEntrega(endereco == null || endereco == ""  ? cliente.getEndereco() : endereco);
        pedido.setItens(itens);
        pedido.setValorPedido(valorPedido);
        pedidoRepository.save(pedido);

        return new PedidoResponseDTO(pedido);
    }

    @Override
    public void confirmarPagamento(Long idPedido, Long idCliente, String codigoAcesso, MetodoPagamento metodoPagamento) {
        validarCliente(idCliente,codigoAcesso);

        Pedido pedido = verificaPedidoPertenceAoCliente(idPedido,idCliente,codigoAcesso);

        mapPagamentoStrategy.get(metodoPagamento).processaPagamento(pedido);

        pedido.alteraStatus(pedido);
        pedidoRepository.save(pedido);

    }

    private Fornecedor verificaFornecedorValido(Long idFornecedor, String codigoAcesso){
        Fornecedor fornecedor = fornecedorRepository.findById(idFornecedor).orElseThrow(FornecedorNaoExisteException::new);
        if(!fornecedor.getCodigo().equals(codigoAcesso)) throw new CodigoDeAcessoInvalidoException();
        return fornecedor;
    }

    @Override
    public void alterarStatusPedidoParaPronto(Long id, Long idFornecedor, String codigoAcesso) {
        Fornecedor fornecedor = verificaFornecedorValido(idFornecedor,codigoAcesso);
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(PedidoNaoExisteException::new);

        pedido.alteraStatus(pedido);

        pedido.setFornecedor(fornecedor);
        pedidoRepository.save(pedido);
        fornecedor.getPedidos().add(pedido);
        fornecedorRepository.save(fornecedor);
    }

    @Override
    public void alterarStatusPedidoEmEntrega(Long id, Long idFornecedor, String codigoAcesso) {
            Fornecedor fornecedor = verificaFornecedorValido(idFornecedor,codigoAcesso);
            Pedido pedido = verificaPedidoPertenceAoFornecedor(id,idFornecedor,codigoAcesso);

            List<Entregador> entregadoresDisponiveis = fornecedor.getEntregadores().stream()
                    .filter(entregador -> entregador.getStatusEntregador() == StatusEntregador.ATIVO)
                    .collect(Collectors.toList());

            Observer clienteObserver = new ClienteObserver();
            addClienteObserve(clienteObserver);

            if (entregadoresDisponiveis.isEmpty()) {
                for (Observer clienteObserverInterface: this.clientesObserves) {
                    clienteObserverInterface.notificaNenhumEntregadorDisponivelParaEntrega(pedido);
                    throw new CommerceException("Nenhum entregador disponivel");
                }
            }

            Entregador entregadorEscolhido = entregadoresDisponiveis.stream()
                    .min(Comparator.comparingInt(entregador -> entregador.getPedidos().size())).orElseThrow(CommerceException::new);

            pedido.alteraStatus(pedido);

            pedido.setEntregador(entregadorEscolhido);
            entregadorEscolhido.getPedidos().add(pedido);
            entregadorRepository.save(entregadorEscolhido);
            pedidoRepository.save(pedido);
        for (Observer clienteObserverInterface: this.clientesObserves) {
            clienteObserverInterface.notificaPedidoSaiuParaEntrega(pedido);
        }
    }

    private void addFornecedorObserver(Observer fornecedorObserver) {
        if (!this.fornecedoresObservers.contains(fornecedorObserver)) {
            this.fornecedoresObservers.add(fornecedorObserver);
        }
    }

    private void addClienteObserve(Observer clienteObserver) {
        if (!this.clientesObserves.contains(clienteObserver)) {
            this.clientesObserves.add(clienteObserver);
        }
    }

    @Override
    public void alterarStatusPedidoParaEntregue(Long id, Long idCliente, String codigoAcesso) {
        Pedido pedido = verificaPedidoPertenceAoCliente(id, idCliente, codigoAcesso);
        validarCliente(idCliente,codigoAcesso);

        Observer forneObserver = new FornecedorObserver();
        addFornecedorObserver(forneObserver);

        pedido.alteraStatus(pedido);

        pedidoRepository.save(pedido);
        for (Observer fornecedorObserver: this.fornecedoresObservers) {
            fornecedorObserver.notificaPedidoEntregue(pedido);
        }
    }

    private Pedido verificaPedidoPertenceAoFornecedor(Long idPedido, Long idFornecedor, String codigoAcesso) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(PedidoNaoExisteException::new);
        if (!pedido.getFornecedor().getId().equals(idFornecedor) || !pedido.getFornecedor().getCodigo().equals(codigoAcesso)) {
            throw new RuntimeException("Esse pedido não corresponde ao fornecedor informado");
        }
        return pedido;
    }

    @Override
    public void cancelarPedido(Long id, Long idCliente, String codigoAcesso) {
        validarCliente(idCliente,codigoAcesso);
        Pedido pedido = verificaPedidoPertenceAoCliente(id,idCliente,codigoAcesso);
        if(!pedido.getStatusPedido().toString().equals("PEDIDO_RECEBIDO") && !pedido.getStatusPedido().toString().equals("PREPARACAO")) throw new CommerceException("So pode ser cancelado pedidos que não atigiram o status de Pedido pronto");

        pedido.getCliente().getPedidos().remove(pedido);
        pedidoRepository.delete(pedido);
    }

}
