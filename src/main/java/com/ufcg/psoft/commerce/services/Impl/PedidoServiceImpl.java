package com.ufcg.psoft.commerce.services.Impl;

import com.ufcg.psoft.commerce.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.PedidoResponseDTO;
import com.ufcg.psoft.commerce.exception.CafeNaoExisteException;
import com.ufcg.psoft.commerce.exception.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoDeAcessoInvalidoException;
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

    @Override
    public PedidoResponseDTO criar(Long idCliente, String codigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(ClienteNaoExisteException::new);
        if (!cliente.getCodigo().equals(codigoAcesso)) throw new CodigoDeAcessoInvalidoException();

        String endereco = pedidoPostPutRequestDTO.getEnderecoDeEntrega();
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatusPedido(StatusPedido.PEDIDO_RECEBIDO);
        pedido.setEnderecoDeEntrega(endereco == null || endereco == ""  ? cliente.getEndereco() : pedidoPostPutRequestDTO.getEnderecoDeEntrega());
        pedido.setValorPedido(0.0);
        pedido.setItens(new ArrayList<>());

        pedidoRepository.save(pedido);

        double valorPedido = 0.0;
        List<ItemPedido> itens = new ArrayList<>();

        for (int i = 0; i < pedidoPostPutRequestDTO.getCafesId().size(); i++) {
            Cafe cafe = cafeRepository.findById(pedidoPostPutRequestDTO.getCafesId().get(i)).orElseThrow(CafeNaoExisteException::new);

            if (cafe.isPremium() && !cliente.isPremium()) {
                throw new RuntimeException("o cliente não pode adicionar cafés premium no pedido");
            }

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setValorDoItem(cafe.getPreco());
            item.setCafe(cafe);
            itemPedidoRepository.save(item);

            itens.add(item);
            valorPedido += cafe.getPreco();
        }

        pedido.setItens(itens);
        pedido.setValorPedido(valorPedido);
        pedidoRepository.save(pedido);

        return new PedidoResponseDTO(pedido);
    }

    @Override
    public PedidoResponseDTO recuperar(Long idPedido, Long idCliente, String codigoAcesso) {
        return null;
    }

    @Override
    public void remover(Long idPedido, Long idCliente, String codigoAcesso) {

    }

    @Override
    public PedidoResponseDTO alterar(Long idPedido, Long idCliente, String codigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        return null;
    }
}
