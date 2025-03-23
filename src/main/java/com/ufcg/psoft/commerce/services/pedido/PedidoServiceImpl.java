package com.ufcg.psoft.commerce.services.pedido;

import com.ufcg.psoft.commerce.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.PedidoResponseDTO;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.model.Enums.MetodoPagamento;
import com.ufcg.psoft.commerce.model.Enums.StatusEntregador;
import com.ufcg.psoft.commerce.event.EntregadorIndisponivelEvent;
import com.ufcg.psoft.commerce.event.PedidoEntregueEvent;
import com.ufcg.psoft.commerce.event.PedidoEmRotaEvent;
import com.ufcg.psoft.commerce.services.pedido.state.PedidoRecebido;
import com.ufcg.psoft.commerce.services.pedido.strategy.PagamentoStrategy;
import com.ufcg.psoft.commerce.services.pedido.strategy.PagamentoCredito;
import com.ufcg.psoft.commerce.repository.*;
import com.ufcg.psoft.commerce.services.pedido.strategy.PagamentoDebito;
import com.ufcg.psoft.commerce.services.pedido.strategy.PagamentoPix;
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

        pedido.getStatusPedidoState().preparar();
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

        pedido.getStatusPedidoState().finalizarPreparo();

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

            if (entregadoresDisponiveis.isEmpty()) {
                pedido.getCliente().notificaEntregadorIndisponivel(new EntregadorIndisponivelEvent(pedido));
                throw new CommerceException("Nenhum entregador disponivel");
                }

            Entregador entregadorEscolhido = entregadoresDisponiveis.stream()
                    .min(Comparator.comparingInt(entregador -> entregador.getPedidos().size())).orElseThrow(CommerceException::new);

            pedido.getStatusPedidoState().enviarParaEntrega();

            pedido.setEntregador(entregadorEscolhido);
            entregadorEscolhido.getPedidos().add(pedido);
            entregadorRepository.save(entregadorEscolhido);
            pedidoRepository.save(pedido);
            pedido.getCliente().notificaPedidoEmRota(new PedidoEmRotaEvent(pedido,entregadorEscolhido));
    }

    @Override
    public void alterarStatusPedidoParaEntregue(Long id, Long idCliente, String codigoAcesso) {
        Pedido pedido = verificaPedidoPertenceAoCliente(id, idCliente, codigoAcesso);
        validarCliente(idCliente,codigoAcesso);

        pedido.getStatusPedidoState().confirmarEntrega();

        pedidoRepository.save(pedido);
        pedido.getFornecedor().notificaPedidoEntregue(new PedidoEntregueEvent(pedido));
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
