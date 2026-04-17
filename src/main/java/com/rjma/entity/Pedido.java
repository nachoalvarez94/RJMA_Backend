package com.rjma.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "cliente")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private Long numero;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 50)
    private String estado;

    @Column(length = 500)
    private String observaciones;

    @Column(name = "total_bruto", precision = 10, scale = 2)
    private BigDecimal totalBruto;

    @Column(name = "total_descuento", precision = 10, scale = 2)
    private BigDecimal totalDescuento;

    @Column(name = "total_final", precision = 10, scale = 2)
    private BigDecimal totalFinal;

    @Column(name = "importe_cobrado", precision = 10, scale = 2)
    private BigDecimal importeCobrado;

    @Column(name = "importe_pendiente", precision = 10, scale = 2)
    private BigDecimal importePendiente;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cobro", length = 20)
    private EstadoCobro estadoCobro;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
