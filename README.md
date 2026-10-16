# TodoMarket - Microservicio de productos

API REST independiente para gestionar productos, precios, categorías, existencias, lotes y alertas de inventario. Construido con Java 17 y Spring Boot.

## Requisitos

- JDK 17 o superior
- Apache Maven 3.6.3 o superior

## Ejecutar

Desde la raíz de este repositorio:

```powershell
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8183`.

## Endpoints

Base: `http://localhost:8183/api/productos`

- `GET /api/productos` - listar productos
- `GET /api/productos/{id}` - buscar por ID
- `POST /api/productos` - crear; el servicio asigna el ID
- `PUT /api/productos/{id}` - actualizar
- `DELETE /api/productos/{id}` - eliminar
- `GET /api/productos/stock-bajo` - consultar stock bajo
- `GET /api/productos/agotados` - consultar agotados
- `GET /api/productos/vencidos` - consultar vencidos
- `GET /api/productos/proximos-a-vencer` - consultar próximos a vencer
- `GET /api/productos/alertas` - obtener alertas de inventario

La API habilita CORS para el frontend local en `http://localhost:4200`.

## Datos

El repositorio de datos actual es en memoria. Al detener o reiniciar el servicio se vuelven a cargar los datos de ejemplo y se reinicia la secuencia de IDs.
