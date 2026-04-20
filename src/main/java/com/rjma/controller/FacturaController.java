package com.rjma.controller;

import com.rjma.dto.response.FacturaResponseDto;
import com.rjma.service.FacturaPdfService;
import com.rjma.service.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;
    private final FacturaPdfService facturaPdfService;

    @PostMapping("/desde-pedido/{pedidoId}")
    public ResponseEntity<FacturaResponseDto> facturarPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaService.facturarPedido(pedidoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<FacturaResponseDto>> listarTodas() {
        return ResponseEntity.ok(facturaService.listarTodas());
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<Resource> descargarPdf(@PathVariable Long id) {
        return facturaPdfService.descargar(id);
    }
}
