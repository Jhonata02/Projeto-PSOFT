package com.ufcg.psoft.commerce.services.Impl;

import com.ufcg.psoft.commerce.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.PedidoResponseDTO;
import com.ufcg.psoft.commerce.exception.CafeNaoExisteException;
import com.ufcg.psoft.commerce.exception.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.CafeRepository;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.ItemPedidoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.services.PedidoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    ModelMapper modelMapper;

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
        pedido.setStatusPedido(StatusPedido.PEDIDO_RECEBIDO);
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
            throw new RuntimeException("Esse pedido não corresponde ao cliente informado");
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
    public void remover(Long idPedido, Long idCliente, String codigoAcesso) {
        validarCliente(idCliente,codigoAcesso);
        Pedido pedido = verificaPedidoPertenceAoCliente(idPedido,idCliente,codigoAcesso);
        pedidoRepository.delete(pedido);
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
        if (pedido.getMetodoPagamento() != null) {
            throw new RuntimeException("O pedido ja foi pago");
        }

        if (metodoPagamento == MetodoPagamento.CREDITO) {
            pedido.setMetodoPagamento(MetodoPagamento.CREDITO);
        }

        if (metodoPagamento == MetodoPagamento.DEBITO) {
            pedido.setMetodoPagamento(MetodoPagamento.DEBITO);
            pedido.setValorPedido(pedido.getValorPedido()*0.975);
        }
        if (metodoPagamento == MetodoPagamento.PIX) {
            pedido.setMetodoPagamento(MetodoPagamento.PIX);
            pedido.setValorPedido(pedido.getValorPedido()*0.95);
        }
        pedido.setStatusPedido(StatusPedido.PREPARACAO);
        pedidoRepository.save(pedido);

    }
}
