package com.rjma.entity;

/**
 * Indica cómo se comercializa un artículo.
 * Valor por defecto para nuevos artículos y datos existentes sin migrar: UNIDAD.
 */
public enum UnidadVenta {
    UNIDAD,
    CAJA,
    GRANEL,
    PESO
}
