package com.todomarket.inventory.config;

import com.todomarket.inventory.producto.ProductoRepository;
import com.todomarket.inventory.producto.ProductoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryConfiguration {
    @Bean
    ProductoService productoService(ProductoRepository repository) {
        return new ProductoService(repository);
    }
}
