package com.rjma.specification;

import com.rjma.entity.EstadoCobro;
import com.rjma.entity.Pedido;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class PedidoSpecification {

    private PedidoSpecification() {}

    public static Specification<Pedido> conVendedor(Long vendedorId) {
        return (root, query, cb) -> {
            var join = root.join("creadoPor", JoinType.LEFT);
            return cb.equal(join.get("id"), vendedorId);
        };
    }

    public static Specification<Pedido> conCliente(Long clienteId) {
        return (root, query, cb) -> cb.equal(root.get("cliente").get("id"), clienteId);
    }

    public static Specification<Pedido> conEstadoCobro(EstadoCobro estadoCobro) {
        return (root, query, cb) -> cb.equal(root.get("estadoCobro"), estadoCobro);
    }

    public static Specification<Pedido> conEstado(String estado) {
        return (root, query, cb) -> cb.equal(root.get("estado"), estado);
    }
}
