package com.rjma.mapper;

import com.rjma.dto.response.PedidoLineaResponseDto;
import com.rjma.dto.response.PedidoResponseDto;
import com.rjma.entity.Pedido;
import com.rjma.entity.PedidoLinea;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapper {

    public PedidoResponseDto toResponse(Pedido pedido, List<PedidoLinea> lineas) {
        return PedidoResponseDto.builder()
                .id(pedido.getId())
                .numero(pedido.getNumero())
                .clienteId(pedido.getCliente().getId())
                .fecha(pedido.getFecha())
                .estado(pedido.getEstado())
                .observaciones(pedido.getObservaciones())
                .totalBruto(pedido.getTotalBruto())
                .totalDescuento(pedido.getTotalDescuento())
                .totalFinal(pedido.getTotalFinal())
                .lineas(lineas.stream().map(this::toResponseLinea).toList())
                .createdAt(pedido.getCreatedAt())
                .updatedAt(pedido.getUpdatedAt())
                .build();
    }

    public PedidoLineaResponseDto toResponseLinea(PedidoLinea linea) {
        return PedidoLineaResponseDto.builder()
                .id(linea.getId())
                .articuloId(linea.getArticulo() != null ? linea.getArticulo().getId() : null)
                .nombreArticulo(linea.getNombreArticulo())
                .precioUnitario(linea.getPrecioUnitario())
                .cantidad(linea.getCantidad())
                .subtotal(linea.getSubtotal())
                .descuento(linea.getDescuento())
                .totalLinea(linea.getTotalLinea())
                .build();
    }
}
