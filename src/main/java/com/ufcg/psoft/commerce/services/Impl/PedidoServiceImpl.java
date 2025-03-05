package com.ufcg.psoft.commerce.services.Impl;

import com.ufcg.psoft.commerce.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.PedidoResponseDTO;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.model.Enums.MetodoPagamento;
import com.ufcg.psoft.commerce.model.Enums.StatusEntregador;
import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.repository.*;
import com.ufcg.psoft.commerce.services.PedidoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    /**
    @Override
    public void remover(Long idPedido, Long idCliente, String codigoAcesso) {
        validarCliente(idCliente,codigoAcesso);
        Pedido pedido = verificaPedidoPertenceAoCliente(idPedido,idCliente,codigoAcesso);
        pedidoRepository.delete(pedido);
    }

     */

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

    private Fornecedor verificaFornecedorValido(Long idFornecedor, String codigoAcesso){
        Fornecedor fornecedor = fornecedorRepository.findById(idFornecedor).orElseThrow(FornecedorNaoExisteException::new);
        if(!fornecedor.getCodigo().equals(codigoAcesso)) throw new CodigoDeAcessoInvalidoException();
        return fornecedor;
    }

    @Override
    public void alterarStatusPedidoParaPronto(Long id, Long idFornecedor, String codigoAcesso) {
        Fornecedor fornecedor = verificaFornecedorValido(idFornecedor,codigoAcesso);
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(PedidoNaoExisteException::new);

        if(pedido.getStatusPedido() != StatusPedido.PREPARACAO) {
            throw new RuntimeException("O Status do pedido não é PREPARACAO");
        }
        pedido.setStatusPedido(StatusPedido.PEDIDO_PRONTO);
        pedido.setFornecedor(fornecedor);
        pedidoRepository.save(pedido);
        fornecedor.getPedidos().add(pedido);
        fornecedorRepository.save(fornecedor);
    }

    private void exibeMensagemNenhumEntregadorDisponivel(Pedido pedido) {
        System.out.println(
                "\nCliente: " + pedido.getCliente().getNome()
                        + " Id: " + pedido.getCliente().getId()
                        + "\nSeu pedido de numero: " + pedido.getId()
                        + ", não saiu para entrega, pois não tem entregadores disponiveis no momento."
        );
    }

    @Override
    public void alterarStatusPedidoEmEntrega(Long id, Long idFornecedor, String codigoAcesso) {
            Fornecedor fornecedor = verificaFornecedorValido(idFornecedor,codigoAcesso);
            Pedido pedido = verificaPedidoPertenceAoFornecedor(id,idFornecedor,codigoAcesso);

            if(pedido.getStatusPedido() != StatusPedido.PEDIDO_PRONTO) {
                throw new RuntimeException("O Status do pedido não é PREPARACAO");
            }


            List<Entregador> entregadoresDisponiveis = fornecedor.getEntregadores().stream()
                    .filter(entregador -> entregador.getStatusEntregador() == StatusEntregador.ATIVO)
                    .collect(Collectors.toList());

            if (entregadoresDisponiveis.isEmpty()) {
                exibeMensagemNenhumEntregadorDisponivel(pedido);
                throw new CommerceException("Nenhum entregador disponivel");
            }

            Entregador entregadorEscolhido = entregadoresDisponiveis.stream()
                    .min(Comparator.comparingInt(entregador -> entregador.getPedidos().size())).orElseThrow(CommerceException::new);

            pedido.setEntregador(entregadorEscolhido);
            entregadorEscolhido.getPedidos().add(pedido);
            entregadorRepository.save(entregadorEscolhido);

            pedido.setStatusPedido(StatusPedido.PEDIDO_EM_ENTREGA);
            pedidoRepository.save(pedido);
            exibeMensagemParaCliente(pedido);

    }
    private void exibeMensagemParaCliente(Pedido pedido) {
        System.out.println(
                "\nCliente: " + pedido.getCliente().getNome()
                        + " Id: " + pedido.getCliente().getId()
                        + "\nSeu pedido de numero: " + pedido.getId()
                        + ", saiu para entrega."
                        + "\nDetalhes do entregador:"
                        + "\nNome: " + pedido.getEntregador().getNome()
                        + "\nVeiculo: " + pedido.getEntregador().getTipoVeiculo()
                        + "\nPlaca do veiculo: " + pedido.getEntregador().getPlaca()
                        + "\nCor do veiculo: " + pedido.getEntregador().getCorDoVeiculo()

        );
    }

    @Override
    public void alterarStatusPedidoParaEntregue(Long id, Long idCliente, String codigoAcesso) {
        Pedido pedido = verificaPedidoPertenceAoCliente(id, idCliente, codigoAcesso);
        validarCliente(idCliente,codigoAcesso);

        if (pedido.getStatusPedido() != StatusPedido.PEDIDO_EM_ENTREGA) throw new CommerceException("Esse pedido não está em entrega.");

        pedido.setStatusPedido(StatusPedido.PEDIDO_ENTREGUE);
        pedidoRepository.save(pedido);
        exibeMensagemParaFornecedor(pedido);
    }

    private void exibeMensagemParaFornecedor(Pedido pedido) {
        System.out.println(
                "\nFornecedor: " + pedido.getFornecedor().getNome()
                        + " Id: " + pedido.getFornecedor().getId()
                        + "\nO pedido de numero: " + pedido.getId()
                        + ", foi entregue ao cliente!"
        );
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
        if(pedido.getStatusPedido() != StatusPedido.PEDIDO_RECEBIDO && pedido.getStatusPedido() != StatusPedido.PREPARACAO) throw new CommerceException("So pode ser cancelado pedidos que não atigiram o status de Pedido pronto");

        pedido.getCliente().getPedidos().remove(pedido);
        pedidoRepository.delete(pedido);
    }

}
