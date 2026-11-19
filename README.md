# todomarket-productos

Repositorio del microservicio de Productos para el tercer corte de Diseño de Soluciones. Conserva el CRUD de inventario y sus consultas por estado, stock y vencimiento. Persiste los productos en MongoDB mediante Spring Data.

## Ejecutar

Con Java 17 o posterior, Maven y MongoDB disponibles, ejecuta desde esta carpeta:

```powershell
mvn spring-boot:run
```

La API local escucha en `8183`. Swagger está en [localhost:8183/swagger-ui/index.html](http://localhost:8183/swagger-ui/index.html). El cliente de esta API es el repositorio [todomarket-usuarios](https://github.com/diegooyola1029/todomarket-usuarios).

| Variable | Uso |
| --- | --- |
| `PORT` | Puerto local 8183 por defecto. |
| `MONGODB_URI` | URI MongoDB. Por defecto apunta a la base local `todomarket` en el puerto 27017. |
| `SERVICE_TOKEN` | Credencial interna opcional para el entorno local y requerida al configurar la publicación. Usuarios envía este valor como `INVENTORY_TOKEN`. |

`Dockerfile` y `render.yaml` pertenecen a Productos. Este servicio se compila y se publica desde su propio repositorio. Las operaciones que llegan desde Usuarios pasan por la ruta `/api/productos`.
