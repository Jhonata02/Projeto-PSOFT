package com.ufcg.psoft.commerce.repository;

import com.ufcg.psoft.commerce.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
