package com.rjma.service;

import com.rjma.dto.request.PedidoLineaRequestDto;
import com.rjma.dto.request.PedidoLineaUpdateRequestDto;
import com.rjma.dto.request.PedidoRequestDto;
import com.rjma.dto.request.PedidoUpdateRequestDto;
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

        // 3. Construir líneas y calcular totalFinal
        List<PedidoLinea> lineas = buildLineas(dto.getLineas().stream()
                .map(l -> new LineaInput(l.getArticuloId(), l.getCantidad()))
                .toList());

        BigDecimal totalFinal = calcularTotal(lineas);

        // 4. Calcular cobro
        CobroResult cobro = calcularCobro(dto.getImporteCobrado(), totalFinal);

        // 5. Crear y guardar el pedido
        Pedido pedido = Pedido.builder()
                .numero(numero)
                .cliente(cliente)
                .fecha(LocalDateTime.now())
                .estado("BORRADOR")
                .observaciones(dto.getObservaciones())
                .totalBruto(totalFinal)
                .totalDescuento(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
                .totalFinal(totalFinal)
                .importeCobrado(cobro.importeCobrado())
                .importePendiente(cobro.importePendiente())
                .estadoCobro(cobro.estadoCobro())
                .build();

        pedidoRepository.save(pedido);

        // 6. Asociar pedido a cada línea y guardarlas
        lineas.forEach(l -> l.setPedido(pedido));
        pedidoLineaRepository.saveAll(lineas);

        return pedidoMapper.toResponse(pedido, lineas);
    }

    @Transactional
    public PedidoResponseDto update(Long id, PedidoUpdateRequestDto dto) {

        // 1. Buscar y validar pedido
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));

        if ("FACTURADO".equals(pedido.getEstado())) {
            throw new BadRequestException("No se puede modificar un pedido facturado");
        }

        // 2. Construir nuevas líneas y recalcular total
        List<PedidoLinea> lineas = buildLineas(dto.getLineas().stream()
                .map(l -> new LineaInput(l.getArticuloId(), l.getCantidad()))
                .toList());

        BigDecimal totalFinal = calcularTotal(lineas);

        // 3. Recalcular cobro
        CobroResult cobro = calcularCobro(dto.getImporteCobrado(), totalFinal);

        // 4. Actualizar campos del pedido
        pedido.setObservaciones(dto.getObservaciones());
        pedido.setTotalBruto(totalFinal);
        pedido.setTotalDescuento(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        pedido.setTotalFinal(totalFinal);
        pedido.setImporteCobrado(cobro.importeCobrado());
        pedido.setImportePendiente(cobro.importePendiente());
        pedido.setEstadoCobro(cobro.estadoCobro());

        pedidoRepository.save(pedido);

        // 5. Reemplazar líneas
        pedidoLineaRepository.deleteByPedidoId(id);
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

    // ── Helpers ──────────────────────────────────────────────────────────────

    private record LineaInput(Long articuloId, BigDecimal cantidad) {}

    private List<PedidoLinea> buildLineas(List<LineaInput> inputs) {
        List<PedidoLinea> lineas = new ArrayList<>();
        for (LineaInput input : inputs) {
            Articulo articulo = articuloRepository.findById(input.articuloId())
                    .orElseThrow(() -> new ResourceNotFoundException("Artículo no encontrado: " + input.articuloId()));

            BigDecimal subtotal = articulo.getPrecio()
                    .multiply(input.cantidad())
                    .setScale(2, RoundingMode.HALF_UP);

            lineas.add(PedidoLinea.builder()
                    .articulo(articulo)
                    .nombreArticulo(articulo.getNombre())
                    .codigoArticulo(articulo.getCodigoInterno())
                    .precioUnitario(articulo.getPrecio())
                    .cantidad(input.cantidad())
                    .subtotal(subtotal)
                    .descuento(BigDecimal.ZERO)
                    .totalLinea(subtotal)
                    .build());
        }
        return lineas;
    }

    private BigDecimal calcularTotal(List<PedidoLinea> lineas) {
        return lineas.stream()
                .map(PedidoLinea::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private CobroResult calcularCobro(BigDecimal importeCobradoRaw, BigDecimal totalFinal) {
        BigDecimal importeCobrado = importeCobradoRaw != null
                ? importeCobradoRaw.setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

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

        return new CobroResult(importeCobrado, importePendiente, estadoCobro);
    }

    private record CobroResult(BigDecimal importeCobrado, BigDecimal importePendiente, EstadoCobro estadoCobro) {}
}
