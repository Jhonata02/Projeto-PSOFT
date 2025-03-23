package com.ufcg.psoft.commerce.model.Enums;

import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.services.pedido.state.*;

public enum StatusPedido {
    // Maquina de estados com ENUM
    PEDIDO_RECEBIDO{
        @Override
        public StateStatusDoPedido estadoAtual(Pedido pedido) {
            return new PedidoRecebido(pedido);
        }
    },
    PREPARACAO{
        @Override
        public StateStatusDoPedido estadoAtual(Pedido pedido) {
            return new Preparacao(pedido);
        }
    },
    PEDIDO_PRONTO{
        @Override
        public StateStatusDoPedido estadoAtual(Pedido pedido) {
            return new PedidoPronto(pedido);
        }
    },
    PEDIDO_EM_ENTREGA{
        @Override
        public StateStatusDoPedido estadoAtual(Pedido pedido) {
            return new PedidoEmEntrega(pedido);
        }
    },
    PEDIDO_ENTREGUE{
        @Override
        public StateStatusDoPedido estadoAtual(Pedido pedido) {
            return new PedidoEntregue(pedido);
        }
    };

    public abstract StateStatusDoPedido estadoAtual(Pedido pedido);


}
