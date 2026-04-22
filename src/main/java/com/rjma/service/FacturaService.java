package com.rjma.service;

import com.rjma.auth.CurrentUserProvider;
import com.rjma.dto.response.FacturaResponseDto;
import com.rjma.entity.EstadoCobro;
import com.rjma.entity.Factura;
import com.rjma.entity.FacturaLinea;
import com.rjma.entity.Pedido;
import com.rjma.entity.PedidoLinea;
import com.rjma.entity.Usuario;
import com.rjma.exception.BadRequestException;
import com.rjma.exception.ResourceNotFoundException;
import com.rjma.mapper.FacturaMapper;
import com.rjma.repository.FacturaLineaRepository;
import com.rjma.repository.FacturaRepository;
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

@Service
@RequiredArgsConstructor
public class FacturaService {

    private static final BigDecimal IVA_RATE = new BigDecimal("0.21");
    private static final BigDecimal TIPO_IVA = new BigDecimal("21.00");

    private final PedidoRepository pedidoRepository;
    private final PedidoLineaRepository pedidoLineaRepository;
    private final FacturaRepository facturaRepository;
    private final FacturaLineaRepository facturaLineaRepository;
    private final FacturaMapper facturaMapper;
    private final FacturaPdfService facturaPdfService;
    private final CurrentUserProvider currentUserProvider;

    // ── Vendedor ──────────────────────────────────────────────────────────────

    @Transactional
    public FacturaResponseDto facturarPedido(Long pedidoId) {
        Pedido pedido = validarPedidoFacturable(pedidoId);

        Usuario currentUser = currentUserProvider.getCurrentUser();
        if (pedido.getCreadoPor() != null && !pedido.getCreadoPor().getId().equals(currentUser.getId())) {
            throw new BadRequestException("No tienes permiso para facturar este pedido");
        }

        return ejecutarFacturacion(pedido, currentUser);
    }

    @Transactional(readOnly = true)
    public FacturaResponseDto obtenerPorId(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada: " + id));
        List<FacturaLinea> lineas = facturaLineaRepository.findByFacturaId(id);
        return facturaMapper.toResponse(factura, lineas);
    }

    @Transactional(readOnly = true)
    public List<FacturaResponseDto> listarTodas() {
        Usuario currentUser = currentUserProvider.getCurrentUser();
        return facturaRepository.findByEmitidaPorId(currentUser.getId()).stream()
                .map(f -> facturaMapper.toResponse(f, facturaLineaRepository.findByFacturaId(f.getId())))
                .toList();
    }

    // ── Admin ─────────────────────────────────────────────────────────────────

    /**
     * Factura un pedido sin validar propiedad (uso exclusivo de ADMIN).
     * El admin autenticado queda registrado como emitidaPor.
     */
    @Transactional
    public FacturaResponseDto facturarPedidoAdmin(Long pedidoId) {
        Pedido pedido = validarPedidoFacturable(pedidoId);
        Usuario currentUser = currentUserProvider.getCurrentUser();
        return ejecutarFacturacion(pedido, currentUser);
    }

    @Transactional(readOnly = true)
    public List<FacturaResponseDto> listarTodasAdmin() {
        return facturaRepository.findAll().stream()
                .map(f -> facturaMapper.toResponse(f, facturaLineaRepository.findByFacturaId(f.getId())))
                .toList();
    }

    // ── Helpers privados ──────────────────────────────────────────────────────

    /** Valida que el pedido existe y está en condiciones de ser facturado. */
    private Pedido validarPedidoFacturable(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + pedidoId));

        if ("FACTURADO".equals(pedido.getEstado())) {
            throw new BadRequestException("El pedido " + pedidoId + " ya ha sido facturado");
        }
        if (facturaRepository.existsByPedidoId(pedidoId)) {
            throw new BadRequestException("Ya existe una factura para el pedido " + pedidoId);
        }
        if (!EstadoCobro.COMPLETO.equals(pedido.getEstadoCobro())) {
            throw new BadRequestException("No se puede facturar un pedido con cobro parcial o pendiente");
        }
        return pedido;
    }

    /** Lógica de facturación compartida por vendedor y admin. */
    private FacturaResponseDto ejecutarFacturacion(Pedido pedido, Usuario emitidaPor) {
        List<PedidoLinea> pedidoLineas = pedidoLineaRepository.findByPedidoId(pedido.getId());

        Long numeroFactura = facturaRepository.findTopByOrderByNumeroFacturaDesc()
                .map(f -> f.getNumeroFactura() + 1)
                .orElse(1L);

        List<FacturaLinea> facturaLineas = new ArrayList<>();
        BigDecimal baseImponible = BigDecimal.ZERO;
        BigDecimal totalImpuestos = BigDecimal.ZERO;

        for (PedidoLinea pedidoLinea : pedidoLineas) {
            BigDecimal subtotal = pedidoLinea.getPrecioUnitario()
                    .multiply(pedidoLinea.getCantidad())
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal cuotaIva = subtotal.multiply(IVA_RATE)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal totalLinea = subtotal.add(cuotaIva).setScale(2, RoundingMode.HALF_UP);

            facturaLineas.add(FacturaLinea.builder()
                    .articulo(pedidoLinea.getArticulo())
                    .nombreArticulo(pedidoLinea.getNombreArticulo())
                    .codigoArticulo(pedidoLinea.getCodigoArticulo())
                    .precioUnitario(pedidoLinea.getPrecioUnitario())
                    .cantidad(pedidoLinea.getCantidad())
                    .subtotal(subtotal)
                    .tipoIva(TIPO_IVA)
                    .cuotaIva(cuotaIva)
                    .totalLinea(totalLinea)
                    .build());

            baseImponible = baseImponible.add(subtotal);
            totalImpuestos = totalImpuestos.add(cuotaIva);
        }

        Factura factura = Factura.builder()
                .numeroFactura(numeroFactura)
                .pedido(pedido)
                .cliente(pedido.getCliente())
                .nombreCliente(pedido.getCliente().getNombre())
                .direccionCliente(pedido.getCliente().getDireccion())
                .emailCliente(pedido.getCliente().getEmail())
                .telefonoCliente(pedido.getCliente().getTelefono())
                .documentoFiscalCliente(pedido.getCliente().getDocumentoFiscal())
                .fechaEmision(LocalDateTime.now())
                .estado("EMITIDA")
                .baseImponible(baseImponible.setScale(2, RoundingMode.HALF_UP))
                .impuestos(totalImpuestos.setScale(2, RoundingMode.HALF_UP))
                .total(baseImponible.add(totalImpuestos).setScale(2, RoundingMode.HALF_UP))
                .emitidaPor(emitidaPor)
                .build();

        facturaRepository.save(factura);

        facturaLineas.forEach(l -> l.setFactura(factura));
        facturaLineaRepository.saveAll(facturaLineas);

        pedido.setEstado("FACTURADO");
        pedidoRepository.save(pedido);

        facturaPdfService.generarYGuardar(factura, facturaLineas);

        return facturaMapper.toResponse(factura, facturaLineas);
    }
}
