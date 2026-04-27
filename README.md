# RJMA Backend

Backend REST para la gestión comercial de RJMA, pensado para dar soporte a una aplicación Android de vendedores y a un BackOffice web de administración.

El sistema permite gestionar clientes, artículos, pedidos, facturas y usuarios, separando permisos entre vendedores y administradores.


---

## Funcionalidades principales

### Autenticación y seguridad

- Login mediante usuario y contraseña.
- Generación de token JWT.
- Endpoints protegidos con Bearer Token.
- Roles de usuario:
  - `VENDEDOR`
  - `ADMIN`

### Gestión de clientes

- Alta, edición y consulta de clientes.
- Activación y desactivación de clientes.
- Datos comerciales y fiscales:
  - nombre
  - nombre comercial
  - documento fiscal
  - teléfono
  - email
  - dirección
  - población / municipio

### Gestión de artículos

- Alta, edición y consulta de productos.
- Activación y desactivación de artículos.
- Soporte para:
  - código interno
  - código de barras
  - precio
  - unidad de venta


---

_Última prueba de despliegue automático develop._
