package com.rjma.controller;

import com.rjma.dto.request.PedidoRequestDto;
import com.rjma.dto.request.PedidoUpdateRequestDto;
import com.rjma.dto.response.PedidoResponseDto;
import com.rjma.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDto> crear(@Valid @RequestBody PedidoRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crear(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> update(@PathVariable Long id,
                                                     @Valid @RequestBody PedidoUpdateRequestDto dto) {
        return ResponseEntity.ok(pedidoService.update(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDto>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }
}
