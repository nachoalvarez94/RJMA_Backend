package com.rjma.repository;

import com.rjma.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    Optional<Factura> findTopByOrderByNumeroFacturaDesc();

    boolean existsByPedidoId(Long pedidoId);

    List<Factura> findByEmitidaPorId(Long usuarioId);
}
