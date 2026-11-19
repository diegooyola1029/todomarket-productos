package com.todomarket.inventory.api;

import io.swagger.v3.oas.annotations.media.Schema;

import com.todomarket.inventory.producto.Producto;
import java.time.LocalDate;
import java.util.List;

public record ProductoRequest(
        @Schema(description = "ID numérico del producto. En POST lo asigna el servicio.", example = "42")
        int idProducto,
        @Schema(description = "Nombre único del producto.", example = "Arroz blanco", requiredMode = Schema.RequiredMode.REQUIRED)
        String nombre,
        @Schema(description = "Descripción del producto.", example = "Presentación de 500 g")
        String descripcion,
        @Schema(description = "Costo de compra en COP.", example = "3000")
        double precioCompra,
        @Schema(description = "Precio de venta en COP.", example = "4200")
        double precioVenta,
        @Schema(description = "Unidades en inventario.", example = "12")
        int stock,
        @Schema(description = "Umbral de stock bajo.", example = "4")
        int stockMinimo,
        @Schema(description = "Código del lote.", example = "L-2026-024", requiredMode = Schema.RequiredMode.REQUIRED)
        String lote,
        @Schema(description = "Fecha ISO 8601. Puede omitirse.", example = "2027-09-30", type = "string", format = "date")
        LocalDate fechaVencimiento,
        @Schema(description = "Categorías del producto.", example = "[\"Granos\"]")
        List<String> categorias,
        @Schema(description = "Estado activo o inactivo.", example = "true")
        boolean estado) {

    public Producto aProducto() {
        return aProducto(idProducto);
    }

    public Producto aProducto(int id) {
        Producto producto = new Producto(id, nombre, descripcion, precioCompra, precioVenta,
                stock, stockMinimo, lote, fechaVencimiento, categorias);
        producto.setEstado(estado);
        return producto;
    }
}
