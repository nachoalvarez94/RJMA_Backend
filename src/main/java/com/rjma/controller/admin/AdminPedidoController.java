package com.rjma.controller.admin;

import com.rjma.dto.response.PedidoResponseDto;
import com.rjma.entity.EstadoCobro;
import com.rjma.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/pedidos")
@RequiredArgsConstructor
public class AdminPedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoResponseDto>> listarTodos(
            @RequestParam(required = false) Long vendedorId,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) EstadoCobro estadoCobro,
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(
                pedidoService.listarTodosAdmin(vendedorId, clienteId, estadoCobro, estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorIdAdmin(id));
    }

    @GetMapping("/pendientes-facturacion")
    public ResponseEntity<List<PedidoResponseDto>> pendientesFacturacion() {
        return ResponseEntity.ok(pedidoService.listarPendientesFacturacion());
    }
}
