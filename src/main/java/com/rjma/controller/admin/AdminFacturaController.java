package com.rjma.controller.admin;

import com.rjma.dto.response.FacturacionMasivaResponseDto;
import com.rjma.dto.response.FacturaResponseDto;
import com.rjma.service.FacturacionMasivaService;
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
    private final FacturacionMasivaService facturacionMasivaService;

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
     * Facturación masiva: factura en bloque todos los pedidos cobrados
     * (EstadoCobro=COMPLETO) que aún no estén en estado FACTURADO.
     * Cada pedido se procesa de forma independiente; un error en un pedido
     * no revierte las facturas ya emitidas.
     * Devuelve un resumen con exitosos, fallidos y el detalle de cada resultado.
     */
    @PostMapping("/masiva")
    public ResponseEntity<FacturacionMasivaResponseDto> facturacionMasiva() {
        FacturacionMasivaResponseDto resultado = facturacionMasivaService.ejecutar();
        // 200 OK si todo fue bien; 207 Multi-Status si hubo al menos un fallo parcial
        HttpStatus status = resultado.getFallidos() == 0
                ? HttpStatus.OK
                : HttpStatus.MULTI_STATUS;
        return ResponseEntity.status(status).body(resultado);
    }
}
