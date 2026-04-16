package com.rjma.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "factura_lineas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"factura", "articulo"})
public class FacturaLinea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;

    @Column(name = "nombre_articulo", nullable = false, length = 150)
    private String nombreArticulo;

    @Column(name = "codigo_articulo", length = 50)
    private String codigoArticulo;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tipo_iva", nullable = false, precision = 5, scale = 2)
    private BigDecimal tipoIva;

    @Column(name = "cuota_iva", nullable = false, precision = 10, scale = 2)
    private BigDecimal cuotaIva;

    @Column(name = "total_linea", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalLinea;
}
