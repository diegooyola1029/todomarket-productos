package com.todomarket.inventory.producto;

import com.todomarket.inventory.modelo.AlertaStockObserver;
import java.util.List;

public class ProductoService {
    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public Producto crear(Producto producto) {
        Producto creado = new Producto(repository.siguienteId(), producto.getNombre(),
                producto.getDescripcion(), producto.getPrecioCompra(), producto.getPrecioVenta(),
                producto.getStock(), producto.getStockMinimo(), producto.getLote(),
                producto.getFechaVencimiento(), producto.getCategorias());
        creado.setEstado(producto.isEstado());
        repository.guardar(creado);
        return creado;
    }

    public List<Producto> listar() { return repository.listar(); }

    public Producto buscar(int id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado."));
    }

    public Producto actualizar(Producto datos) {
        repository.actualizar(datos);
        return datos;
    }

    public void eliminar(int id) {
        if (!repository.eliminar(id)) throw new IllegalArgumentException("Producto no encontrado.");
    }

    public List<Producto> stockBajo() {
        return listar().stream().filter(producto -> producto.getStock() > 0 && producto.tieneStockBajo()).toList();
    }
    public List<Producto> agotados() { return listar().stream().filter(Producto::estaAgotado).toList(); }
    public List<Producto> vencidos() { return listar().stream().filter(Producto::estaVencido).toList(); }
    public List<Producto> proximosAVencer() { return listar().stream().filter(Producto::estaProximoAVencer).toList(); }

    public List<String> alertas() {
        AlertaStockObserver observador = new AlertaStockObserver();
        for (Producto producto : listar()) {
            producto.agregarObservador(observador);
            producto.notificarObservadores();
        }
        return observador.getAlertas();
    }
}
