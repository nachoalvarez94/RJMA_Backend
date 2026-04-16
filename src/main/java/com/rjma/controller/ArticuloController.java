package com.rjma.controller;

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
@RequestMapping("/api/articulos")
@RequiredArgsConstructor
public class ArticuloController {

    private final ArticuloService articuloService;

    @PostMapping
    public ResponseEntity<ArticuloResponseDto> crear(@Valid @RequestBody ArticuloRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articuloService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<ArticuloResponseDto>> listarTodos() {
        return ResponseEntity.ok(articuloService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticuloResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(articuloService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticuloResponseDto> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody ArticuloRequestDto dto) {
        return ResponseEntity.ok(articuloService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        articuloService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
