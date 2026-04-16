package com.rjma.repository;

import com.rjma.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);

    Optional<Pedido> findByNumero(Long numero);

    List<Pedido> findByClienteIdOrderByFechaDesc(Long clienteId);
}
