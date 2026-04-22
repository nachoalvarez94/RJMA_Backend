package com.rjma.service;

import com.rjma.dto.response.FacturacionMasivaResponseDto;
import com.rjma.dto.response.FacturacionMasivaResponseDto.ErrorEntry;
import com.rjma.dto.response.FacturaResponseDto;
import com.rjma.entity.EstadoCobro;
import com.rjma.entity.Pedido;
import com.rjma.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Orquesta la facturación masiva de todos los pedidos pendientes.
 *
 * <p>Cada pedido se factura de forma independiente a través de
 * {@link FacturaService#facturarPedidoAdmin(Long)}, que tiene su propio
 * contexto transaccional. De este modo un fallo en un pedido concreto
 * no revierte las facturas ya emitidas correctamente.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FacturacionMasivaService {

    private final PedidoRepository pedidoRepository;
    private final FacturaService facturaService;

    /**
     * Factura en bloque todos los pedidos cobrados (EstadoCobro=COMPLETO)
     * que aún no han sido facturados (estado ≠ "FACTURADO").
     *
     * @return resumen con el número de éxitos, fallos y el detalle de cada resultado.
     */
    public FacturacionMasivaResponseDto ejecutar() {
        List<Pedido> candidatos = pedidoRepository
                .findByEstadoCobroAndEstadoNot(EstadoCobro.COMPLETO, "FACTURADO");

        List<FacturaResponseDto> facturas = new ArrayList<>();
        List<ErrorEntry> errores = new ArrayList<>();

        for (Pedido pedido : candidatos) {
            try {
                FacturaResponseDto factura = facturaService.facturarPedidoAdmin(pedido.getId());
                facturas.add(factura);
                log.info("Facturación masiva — pedido {} facturado OK (factura {})",
                        pedido.getId(), factura.getNumeroFactura());
            } catch (Exception ex) {
                log.warn("Facturación masiva — pedido {} falló: {}", pedido.getId(), ex.getMessage());
                errores.add(ErrorEntry.builder()
                        .pedidoId(pedido.getId())
                        .motivo(ex.getMessage())
                        .build());
            }
        }

        return FacturacionMasivaResponseDto.builder()
                .total(candidatos.size())
                .exitosos(facturas.size())
                .fallidos(errores.size())
                .facturas(facturas)
                .errores(errores)
                .build();
    }
}
