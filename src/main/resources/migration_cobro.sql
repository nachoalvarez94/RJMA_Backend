-- Migración: inicializar campos de cobro en pedidos existentes
-- Ejecutar manualmente en PostgreSQL una sola vez tras desplegar la nueva versión.
-- Hibernate (ddl-auto=update) añade las columnas como nullable; este script las rellena.

-- 1. Pedidos sin estado de cobro → PENDIENTE (cobro 0, pendiente = total)
UPDATE pedidos
SET importe_cobrado  = 0.00,
    importe_pendiente = COALESCE(total_final, 0.00),
    estado_cobro     = 'PENDIENTE'
WHERE estado_cobro IS NULL;

-- 2. (Opcional) Marcar manualmente como COMPLETO pedidos ya cobrados.
--    Ajusta el criterio según tu lógica de negocio. Ejemplo:
--    pedidos en estado FACTURADO que se asumen cobrados.
--
-- UPDATE pedidos
-- SET importe_cobrado  = total_final,
--     importe_pendiente = 0.00,
--     estado_cobro     = 'COMPLETO'
-- WHERE estado = 'FACTURADO'
--   AND estado_cobro = 'PENDIENTE';
