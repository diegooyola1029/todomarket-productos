package com.todomarket.inventory.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.todomarket.inventory.producto.Producto;
import com.todomarket.inventory.producto.ProductoService;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Inventario", description = "Productos, existencias y alertas de inventario.")
@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {
    private final ProductoService service;

    public ProductoRestController(ProductoService service) { this.service = service; }

    @Operation(summary = "Listar productos", description = "Devuelve el listado de registros ordenado por ID.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "503", description = "MongoDB no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"MongoDB no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping
    public List<ProductoResponse> listar() { return respuestasDe(service.listar()); }

    @Operation(summary = "Consultar por ID", description = "Devuelve un registro por el identificador numérico de la ruta.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Identificador o datos inválidos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El identificador o el cuerpo de la solicitud tiene un formato inválido.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "404", description = "No se encontró el registro.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"Producto no encontrado.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "503", description = "MongoDB no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"MongoDB no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping("/{id}")
    public ProductoResponse buscar(@Parameter(description = "ID numérico del registro", example = "42") @PathVariable int id) { return ProductoResponse.desde(service.buscar(id)); }

    @Operation(summary = "Crear", description = "Asigna un ID y devuelve el registro creado. El encabezado Location indica cómo consultarlo.")
    @ApiResponse(responseCode = "201", description = "Registro creado.", useReturnTypeSchema = true,
            headers = @Header(name = "Location", description = "Ruta de consulta del registro."))
    @ApiResponse(responseCode = "400", description = "Identificador o datos inválidos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El identificador o el cuerpo de la solicitud tiene un formato inválido.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "409", description = "Los datos deben ser únicos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"Ya existe un producto con ese nombre.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "503", description = "MongoDB no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"MongoDB no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@RequestBody ProductoRequest solicitud) {
        Producto creado = service.crear(solicitud.aProducto(1));
        return ResponseEntity.created(URI.create("/api/productos/" + creado.getIdProducto()))
                .body(ProductoResponse.desde(creado));
    }

    @Operation(summary = "Actualizar", description = "Sustituye los datos editables. Si el cuerpo incluye un ID, debe coincidir con la ruta.")
    @ApiResponse(responseCode = "200", description = "Registro actualizado.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Identificador o datos inválidos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El identificador o el cuerpo de la solicitud tiene un formato inválido.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "404", description = "No se encontró el registro.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"Producto no encontrado.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "409", description = "Los datos deben ser únicos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"Ya existe un producto con ese nombre.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "503", description = "MongoDB no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"MongoDB no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @PutMapping("/{id}")
    public ProductoResponse actualizar(@Parameter(description = "ID numérico del registro", example = "42") @PathVariable int id, @RequestBody ProductoRequest solicitud) {
        if (solicitud.idProducto() != 0 && id != solicitud.idProducto()) {
            throw new IllegalArgumentException("El ID de la ruta y del producto deben coincidir.");
        }
        return ProductoResponse.desde(service.actualizar(solicitud.aProducto(id)));
    }

    @Operation(summary = "Eliminar", description = "Borra el registro identificado por la ruta y responde con el cuerpo vacío.")
    @ApiResponse(responseCode = "204", description = "Registro eliminado.", content = @Content)
    @ApiResponse(responseCode = "400", description = "Identificador o datos inválidos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El identificador o el cuerpo de la solicitud tiene un formato inválido.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "404", description = "No se encontró el registro.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"Producto no encontrado.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "503", description = "MongoDB no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"MongoDB no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID numérico del registro", example = "42") @PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar productos con stock bajo", description = "Devuelve productos con existencias positivas iguales o inferiores al mínimo.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "503", description = "MongoDB no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"MongoDB no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping("/stock-bajo")
    public List<ProductoResponse> stockBajo() { return respuestasDe(service.stockBajo()); }

    @Operation(summary = "Listar productos agotados", description = "Devuelve productos sin existencias.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "503", description = "MongoDB no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"MongoDB no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping("/agotados")
    public List<ProductoResponse> agotados() { return respuestasDe(service.agotados()); }

    @Operation(summary = "Listar productos vencidos", description = "Devuelve productos cuya fecha de vencimiento es anterior a hoy.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "503", description = "MongoDB no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"MongoDB no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping("/vencidos")
    public List<ProductoResponse> vencidos() { return respuestasDe(service.vencidos()); }

    @Operation(summary = "Listar productos próximos a vencer", description = "Devuelve productos que vencen entre hoy y los próximos 30 días, inclusive.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "503", description = "MongoDB no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"MongoDB no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping("/proximos-a-vencer")
    public List<ProductoResponse> proximosAVencer() { return respuestasDe(service.proximosAVencer()); }

    @Operation(summary = "Consultar alertas de inventario", description = "Devuelve mensajes de agotamiento, stock bajo y vencimiento próximo calculados a partir de MongoDB.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "503", description = "MongoDB no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"MongoDB no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping("/alertas")
    public List<String> alertas() { return service.alertas(); }

    private List<ProductoResponse> respuestasDe(List<Producto> productos) {
        return productos.stream().map(ProductoResponse::desde).toList();
    }
}
