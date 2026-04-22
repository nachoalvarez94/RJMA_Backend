package com.rjma.controller.admin;

import com.rjma.dto.request.ArticuloRequestDto;
import com.rjma.dto.response.ArticuloResponseDto;
import com.rjma.service.ArticuloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/productos")
@RequiredArgsConstructor
public class AdminArticuloController {

    private final ArticuloService articuloService;

    @GetMapping
    public ResponseEntity<List<ArticuloResponseDto>> listarTodos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Boolean activo) {
        return ResponseEntity.ok(articuloService.buscarAdmin(nombre, activo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticuloResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(articuloService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ArticuloResponseDto> crear(@Valid @RequestBody ArticuloRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articuloService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticuloResponseDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ArticuloRequestDto dto) {
        return ResponseEntity.ok(articuloService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<ArticuloResponseDto> activar(@PathVariable Long id) {
        return ResponseEntity.ok(articuloService.activar(id));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ArticuloResponseDto> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(articuloService.desactivar(id));
    }
}
