package com.rjma.controller.admin;

import com.rjma.dto.response.FacturaResponseDto;
import com.rjma.service.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/facturas")
@RequiredArgsConstructor
public class AdminFacturaController {

    private final FacturaService facturaService;

    @GetMapping
    public ResponseEntity<List<FacturaResponseDto>> listarTodas() {
        return ResponseEntity.ok(facturaService.listarTodasAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.obtenerPorId(id));
    }

    @PostMapping("/desde-pedido/{pedidoId}")
    public ResponseEntity<FacturaResponseDto> facturarPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facturaService.facturarPedidoAdmin(pedidoId));
    }

    /**
     * Facturación masiva trimestral — pendiente de implementar.
     * Factura en una sola operación todos los pedidos cobrados y no facturados.
     * Ver: POST /api/admin/pedidos/pendientes-facturacion para obtener el listado previo.
     */
    @PostMapping("/masiva")
    public ResponseEntity<Void> facturacionMasiva() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
