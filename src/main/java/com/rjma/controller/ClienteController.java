package com.rjma.controller;

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
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDto> crear(@Valid @RequestBody ClienteRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody ClienteRequestDto dto) {
        return ResponseEntity.ok(clienteService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
