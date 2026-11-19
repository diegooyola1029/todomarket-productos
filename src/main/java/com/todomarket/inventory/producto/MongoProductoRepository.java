package com.todomarket.inventory.producto;

import java.util.List;
import java.util.Optional;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReplaceOptions;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
class MongoProductoRepository implements ProductoRepository {
    private final MongoTemplate mongo;

    MongoProductoRepository(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    @Override
    public int siguienteId() {
        Secuencia secuencia = mongo.findAndModify(
                porId("productos"), new Update().inc("valor", 1),
                FindAndModifyOptions.options().upsert(true).returnNew(true), Secuencia.class);
        return Math.toIntExact(secuencia.valor());
    }

    @Override
    public void guardar(Producto producto) {
        mongo.insert(ProductoDocumento.desde(producto));
    }

    @Override
    public List<Producto> listar() {
        return mongo.find(new Query().with(Sort.by("_id")), ProductoDocumento.class)
                .stream().map(ProductoDocumento::aProducto).toList();
    }

    @Override
    public Optional<Producto> buscarPorId(int id) {
        return Optional.ofNullable(mongo.findById(id, ProductoDocumento.class))
                .map(ProductoDocumento::aProducto);
    }

    @Override
    public void actualizar(Producto producto) {
        var resultado = mongo.replace(porId(producto.getIdProducto()), ProductoDocumento.desde(producto),
                ReplaceOptions.replaceOptions());
        if (resultado.getMatchedCount() == 0) throw new IllegalArgumentException("Producto no encontrado.");
    }

    @Override
    public boolean eliminar(int id) {
        return mongo.remove(porId(id), ProductoDocumento.class).getDeletedCount() == 1;
    }

    private static Query porId(Object id) {
        return Query.query(Criteria.where("_id").is(id));
    }

    @Document("secuencias")
    private record Secuencia(@Id String id, long valor) { }
}
