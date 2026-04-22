package com.rjma.controller.admin;

import com.rjma.dto.request.CambioRolRequestDto;
import com.rjma.dto.request.UsuarioRequestDto;
import com.rjma.dto.request.UsuarioUpdateRequestDto;
import com.rjma.dto.response.UsuarioResponseDto;
import com.rjma.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> crear(@Valid @RequestBody UsuarioRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequestDto dto) {
        return ResponseEntity.ok(usuarioService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/rol")
    public ResponseEntity<UsuarioResponseDto> cambiarRol(
            @PathVariable Long id,
            @Valid @RequestBody CambioRolRequestDto dto) {
        return ResponseEntity.ok(usuarioService.cambiarRol(id, dto));
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<UsuarioResponseDto> activar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.activar(id));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<UsuarioResponseDto> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.desactivar(id));
    }
}
