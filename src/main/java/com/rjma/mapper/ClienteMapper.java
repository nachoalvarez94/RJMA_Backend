package com.rjma.mapper;

import com.rjma.dto.request.ClienteRequestDto;
import com.rjma.dto.response.ClienteResponseDto;
import com.rjma.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteResponseDto toResponse(Cliente cliente) {
        return ClienteResponseDto.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .direccion(cliente.getDireccion())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .nombreComercio(cliente.getNombreComercio())
                .poblacion(cliente.getPoblacion())
                .municipio(cliente.getMunicipio())
                .documentoFiscal(cliente.getDocumentoFiscal())
                .activo(cliente.getActivo())
                .createdAt(cliente.getCreatedAt())
                .updatedAt(cliente.getUpdatedAt())
                .build();
    }

    public Cliente toEntity(ClienteRequestDto dto) {
        return Cliente.builder()
                .nombre(dto.getNombre())
                .direccion(dto.getDireccion())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .nombreComercio(dto.getNombreComercio())
                .poblacion(dto.getPoblacion())
                .municipio(dto.getMunicipio())
                .documentoFiscal(dto.getDocumentoFiscal())
                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                .build();
    }

    public void updateEntity(Cliente cliente, ClienteRequestDto dto) {
        cliente.setNombre(dto.getNombre());
        cliente.setDireccion(dto.getDireccion());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setNombreComercio(dto.getNombreComercio());
        cliente.setPoblacion(dto.getPoblacion());
        cliente.setMunicipio(dto.getMunicipio());
        cliente.setDocumentoFiscal(dto.getDocumentoFiscal());
        if (dto.getActivo() != null) {
            cliente.setActivo(dto.getActivo());
        }
    }
}
