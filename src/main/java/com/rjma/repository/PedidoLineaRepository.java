package com.rjma.repository;

import com.rjma.entity.PedidoLinea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoLineaRepository extends JpaRepository<PedidoLinea, Long> {

    List<PedidoLinea> findByPedidoId(Long pedidoId);

    void deleteByPedidoId(Long pedidoId);
}
