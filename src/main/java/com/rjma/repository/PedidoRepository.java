package com.rjma.repository;

import com.rjma.entity.EstadoCobro;
import com.rjma.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido> {

    List<Pedido> findByClienteId(Long clienteId);

    Optional<Pedido> findByNumero(Long numero);

    List<Pedido> findByClienteIdOrderByFechaDesc(Long clienteId);

    Optional<Pedido> findTopByOrderByNumeroDesc();

    List<Pedido> findByCreadoPorId(Long usuarioId);

    // Admin: pedidos pendientes de facturar (cobro completo pero aún no facturados)
    List<Pedido> findByEstadoCobroAndEstadoNot(EstadoCobro estadoCobro, String estado);
}
