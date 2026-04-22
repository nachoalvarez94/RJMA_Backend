package com.rjma.controller.admin;

import com.rjma.dto.request.ClienteRequestDto;
import com.rjma.dto.response.ClienteResponseDto;
import com.rjma.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/clientes")
@RequiredArgsConstructor
public class AdminClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listarTodos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Boolean activo) {
        return ResponseEntity.ok(clienteService.buscarAdmin(nombre, activo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDto> crear(@Valid @RequestBody ClienteRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDto dto) {
        return ResponseEntity.ok(clienteService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<ClienteResponseDto> activar(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.activar(id));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ClienteResponseDto> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.desactivar(id));
    }
}
