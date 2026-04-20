-- Migración: asociar pedidos/facturas existentes al usuario vendedor1
-- Ejecutar manualmente en PostgreSQL una sola vez tras desplegar la nueva versión.
--
-- NOTA IMPORTANTE (fase 2 — Spring Security + JWT):
--   Los usuarios (vendedor1, admin) ya NO se insertan aquí con contraseñas en claro.
--   El DataInitializer crea los usuarios automáticamente en el arranque de la app
--   con contraseñas cifradas correctamente con BCryptPasswordEncoder.
--   Basta con arrancar la aplicación; DataInitializer es idempotente (usa ON CONFLICT implícito
--   via findByUsername antes de insertar).
--
--   Si aun así necesitas insertar un usuario manualmente, genera el hash con:
--     SELECT crypt('tu_contraseña', gen_salt('bf', 10));      -- PostgreSQL con pgcrypto
--   O en Java:
--     new BCryptPasswordEncoder().encode("tu_contraseña")

-- 1. Asociar pedidos existentes sin creador al usuario vendedor1
--    (seguro ejecutar tras el primer arranque de la app)
UPDATE pedidos
SET creado_por_id = (SELECT id FROM usuarios WHERE username = 'vendedor1')
WHERE creado_por_id IS NULL;

-- 2. Asociar facturas existentes sin emisor al usuario vendedor1
UPDATE facturas
SET emitida_por_id = (SELECT id FROM usuarios WHERE username = 'vendedor1')
WHERE emitida_por_id IS NULL;
