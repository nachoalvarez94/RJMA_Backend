-- Migración: crear usuarios iniciales y asociar pedidos/facturas existentes
-- Ejecutar manualmente en PostgreSQL una sola vez tras desplegar la nueva versión.
-- Hibernate (ddl-auto=update) crea la tabla usuarios y añade las columnas FK como nullable;
-- este script inserta el usuario de desarrollo y backfilla las FKs huérfanas.

-- 1. Insertar usuario de desarrollo (vendedor por defecto)
--    passwordHash es un placeholder; en producción se usará el hash real de BCrypt.
INSERT INTO usuarios (username, password_hash, nombre, email, activo, rol, created_at, updated_at)
VALUES ('vendedor1', '$2a$10$placeholder_hash_reemplazar_en_produccion', 'Vendedor Uno', 'vendedor1@rjma.com', true, 'VENDEDOR', NOW(), NOW())
ON CONFLICT (username) DO NOTHING;

-- 2. (Opcional) Insertar usuario administrador
-- INSERT INTO usuarios (username, password_hash, nombre, email, activo, rol, created_at, updated_at)
-- VALUES ('admin', '$2a$10$placeholder_hash_reemplazar_en_produccion', 'Administrador', 'admin@rjma.com', true, 'ADMIN', NOW(), NOW())
-- ON CONFLICT (username) DO NOTHING;

-- 3. Asociar pedidos existentes sin creador al usuario vendedor1
UPDATE pedidos
SET creado_por_id = (SELECT id FROM usuarios WHERE username = 'vendedor1')
WHERE creado_por_id IS NULL;

-- 4. Asociar facturas existentes sin emisor al usuario vendedor1
UPDATE facturas
SET emitida_por_id = (SELECT id FROM usuarios WHERE username = 'vendedor1')
WHERE emitida_por_id IS NULL;
