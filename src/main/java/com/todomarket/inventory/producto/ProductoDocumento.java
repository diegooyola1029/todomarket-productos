package com.todomarket.inventory.producto;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("productos")
record ProductoDocumento(
        @Id int id,
        String nombre,
        @Indexed(unique = true) String nombreNormalizado,
        String descripcion,
        double precioCompra,
        double precioVenta,
        int stock,
        int stockMinimo,
        String lote,
        LocalDate fechaVencimiento,
        List<String> categorias,
        boolean estado) {

    static ProductoDocumento desde(Producto producto) {
        return new ProductoDocumento(producto.getIdProducto(), producto.getNombre(),
                producto.getNombre().toLowerCase(Locale.ROOT), producto.getDescripcion(),
                producto.getPrecioCompra(), producto.getPrecioVenta(), producto.getStock(),
                producto.getStockMinimo(), producto.getLote(), producto.getFechaVencimiento(),
                producto.getCategorias(), producto.isEstado());
    }

    Producto aProducto() {
        Producto producto = new Producto(id, nombre, descripcion, precioCompra, precioVenta,
                stock, stockMinimo, lote, fechaVencimiento, categorias);
        producto.setEstado(estado);
        return producto;
    }
}
