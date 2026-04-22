package com.rjma.service;

import com.rjma.dto.request.ClienteRequestDto;
import com.rjma.dto.response.ClienteResponseDto;
import com.rjma.entity.Cliente;
import com.rjma.exception.ResourceNotFoundException;
import com.rjma.mapper.ClienteMapper;
import com.rjma.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Transactional
    public ClienteResponseDto crear(ClienteRequestDto dto) {
        Cliente cliente = clienteMapper.toEntity(dto);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public ClienteResponseDto obtenerPorId(Long id) {
        return clienteMapper.toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDto> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDto> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(clienteMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDto> buscarPorNombreComercio(String nombreComercio) {
        return clienteRepository.findByNombreComercioContainingIgnoreCase(nombreComercio).stream()
                .map(clienteMapper::toResponse)
                .toList();
    }

    @Transactional
    public ClienteResponseDto actualizar(Long id, ClienteRequestDto dto) {
        Cliente cliente = findOrThrow(id);
        clienteMapper.updateEntity(cliente, dto);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = findOrThrow(id);
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    @Transactional
    public ClienteResponseDto activar(Long id) {
        Cliente cliente = findOrThrow(id);
        cliente.setActivo(true);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponseDto desactivar(Long id) {
        Cliente cliente = findOrThrow(id);
        cliente.setActivo(false);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDto> buscarAdmin(String nombre, Boolean activo) {
        List<Cliente> result;
        if (nombre != null && activo != null) {
            result = clienteRepository.findByActivoAndNombreContainingIgnoreCase(activo, nombre);
        } else if (nombre != null) {
            result = clienteRepository.findByNombreContainingIgnoreCase(nombre);
        } else if (activo != null) {
            result = clienteRepository.findByActivo(activo);
        } else {
            result = clienteRepository.findAll();
        }
        return result.stream().map(clienteMapper::toResponse).toList();
    }

    private Cliente findOrThrow(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado: " + id));
    }
}
