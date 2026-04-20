package com.rjma.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "facturas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"pedido", "cliente", "emitidaPor"})
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "numero_factura", nullable = false, unique = true)
    private Long numeroFactura;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "nombre_cliente", nullable = false, length = 150)
    private String nombreCliente;

    @Column(name = "direccion_cliente", length = 255)
    private String direccionCliente;

    @Column(name = "email_cliente", length = 150)
    private String emailCliente;

    @Column(name = "telefono_cliente", length = 20)
    private String telefonoCliente;

    @Column(name = "documento_fiscal_cliente", length = 20)
    private String documentoFiscalCliente;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(length = 50)
    private String estado;

    @Column(name = "base_imponible", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseImponible;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal impuestos;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emitida_por_id")
    private Usuario emitidaPor;

    @Column(name = "pdf_path", length = 500)
    private String pdfPath;

    @Column(name = "pdf_file_name", length = 100)
    private String pdfFileName;

    @Column(name = "pdf_version")
    private Integer pdfVersion;

    @Column(name = "pdf_generated_at")
    private LocalDateTime pdfGeneratedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
