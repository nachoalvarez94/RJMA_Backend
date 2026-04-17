package com.rjma.service;

import com.rjma.dto.request.PedidoLineaRequestDto;
import com.rjma.dto.request.PedidoRequestDto;
import com.rjma.dto.response.PedidoResponseDto;
import com.rjma.entity.Articulo;
import com.rjma.entity.Cliente;
import com.rjma.entity.EstadoCobro;
import com.rjma.entity.Pedido;
import com.rjma.entity.PedidoLinea;
import com.rjma.exception.BadRequestException;
import com.rjma.exception.ResourceNotFoundException;
import com.rjma.mapper.PedidoMapper;
import com.rjma.repository.ArticuloRepository;
import com.rjma.repository.ClienteRepository;
import com.rjma.repository.PedidoLineaRepository;
import com.rjma.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final ClienteRepository clienteRepository;
    private final ArticuloRepository articuloRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoLineaRepository pedidoLineaRepository;
    private final PedidoMapper pedidoMapper;

    @Transactional
    public PedidoResponseDto crear(PedidoRequestDto dto) {

        // 1. Validar cliente
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado: " + dto.getClienteId()));

        // 2. Generar número de pedido
        Long numero = pedidoRepository.findTopByOrderByNumeroDesc()
                .map(p -> p.getNumero() + 1)
                .orElse(1L);

        // 3. Construir líneas y calcular totalBruto
        List<PedidoLinea> lineas = new ArrayList<>();
        BigDecimal totalBruto = BigDecimal.ZERO;

        for (PedidoLineaRequestDto lineaDto : dto.getLineas()) {

            Articulo articulo = articuloRepository.findById(lineaDto.getArticuloId())
                    .orElseThrow(() -> new ResourceNotFoundException("Artículo no encontrado: " + lineaDto.getArticuloId()));

            BigDecimal subtotal = articulo.getPrecio()
                    .multiply(lineaDto.getCantidad())
                    .setScale(2, RoundingMode.HALF_UP);

            PedidoLinea linea = PedidoLinea.builder()
                    .articulo(articulo)
                    .nombreArticulo(articulo.getNombre())
                    .codigoArticulo(articulo.getCodigoInterno())
                    .precioUnitario(articulo.getPrecio())
                    .cantidad(lineaDto.getCantidad())
                    .subtotal(subtotal)
                    .descuento(BigDecimal.ZERO)
                    .totalLinea(subtotal)
                    .build();

            lineas.add(linea);
            totalBruto = totalBruto.add(subtotal);
        }

        BigDecimal totalFinal = totalBruto.setScale(2, RoundingMode.HALF_UP);

        // 4. Calcular estado de cobro
        BigDecimal importeCobrado = dto.getImporteCobrado() != null
                ? dto.getImporteCobrado().setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        if (importeCobrado.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("El importe cobrado no puede ser negativo");
        }
        if (importeCobrado.compareTo(totalFinal) > 0) {
            throw new BadRequestException("El importe cobrado no puede superar el total del pedido");
        }

        EstadoCobro estadoCobro;
        if (importeCobrado.compareTo(totalFinal) == 0) {
            estadoCobro = EstadoCobro.COMPLETO;
        } else if (importeCobrado.compareTo(BigDecimal.ZERO) == 0) {
            estadoCobro = EstadoCobro.PENDIENTE;
        } else {
            estadoCobro = EstadoCobro.PARCIAL;
        }

        BigDecimal importePendiente = totalFinal.subtract(importeCobrado).setScale(2, RoundingMode.HALF_UP);

        // 5. Crear y guardar el pedido
        Pedido pedido = Pedido.builder()
                .numero(numero)
                .cliente(cliente)
                .fecha(LocalDateTime.now())
                .estado("BORRADOR")
                .observaciones(dto.getObservaciones())
                .totalBruto(totalFinal)
                .totalDescuento(BigDecimal.ZERO)
                .totalFinal(totalFinal)
                .importeCobrado(importeCobrado)
                .importePendiente(importePendiente)
                .estadoCobro(estadoCobro)
                .build();

        pedidoRepository.save(pedido);

        // 6. Asociar pedido a cada línea y guardarlas
        lineas.forEach(l -> l.setPedido(pedido));
        pedidoLineaRepository.saveAll(lineas);

        return pedidoMapper.toResponse(pedido, lineas);
    }

    @Transactional(readOnly = true)
    public PedidoResponseDto obtenerPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
        List<PedidoLinea> lineas = pedidoLineaRepository.findByPedidoId(id);
        return pedidoMapper.toResponse(pedido, lineas);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDto> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(p -> pedidoMapper.toResponse(p, pedidoLineaRepository.findByPedidoId(p.getId())))
                .collect(Collectors.toList());
    }
}
