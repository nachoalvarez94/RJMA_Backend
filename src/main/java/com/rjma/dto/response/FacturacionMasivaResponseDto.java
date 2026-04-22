package com.rjma.dto.response;

import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * Resultado de una operación de facturación masiva.
 * Incluye el recuento global y el detalle de cada pedido procesado.
 */
@Value
@Builder
public class FacturacionMasivaResponseDto {

    /** Total de pedidos candidatos procesados. */
    int total;

    /** Pedidos facturados con éxito. */
    int exitosos;

    /** Pedidos que fallaron durante la facturación. */
    int fallidos;

    /** Facturas generadas correctamente. */
    List<FacturaResponseDto> facturas;

    /** Detalle de los errores producidos. */
    List<ErrorEntry> errores;

    @Value
    @Builder
    public static class ErrorEntry {
        Long pedidoId;
        String motivo;
    }
}
