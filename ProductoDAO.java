package edu.umg;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private final MongoCollection<Document> coleccion;

    public ProductoDAO() {
        MongoClient cliente = ConexionMongo.conectar();
        MongoDatabase db = cliente.getDatabase("tienda");
        this.coleccion = db.getCollection("productos");
    }

    // ACTIVIDAD 1: Insertar
    public void insertar(Producto p) {
        Document doc = new Document("codigo", p.getCodigo())
                .append("nombre", p.getNombre())
                .append("categoria", p.getCategoria())
                .append("precio", p.getPrecio())
                .append("existencia", p.getExistencia());
        coleccion.insertOne(doc);
    }

    // ACTIVIDAD 2: Listar
    public List<Document> listar() {
        List<Document> lista = new ArrayList<>();
        coleccion.find().into(lista);
        return lista;
    }

    // ACTIVIDAD 3: Buscar por Código
    public Document buscarPorCodigo(String codigo) {
        return coleccion.find(Filters.eq("codigo", codigo)).first();
    }

    // ACTIVIDAD 4: Actualizar Existencia
    public boolean actualizarExistencia(String codigo, int nuevaExistencia) {
        var res = coleccion.updateOne(Filters.eq("codigo", codigo), Updates.set("existencia", nuevaExistencia));
        return res.getModifiedCount() > 0;
    }

    // ACTIVIDAD 5: Actualizar Precio
    public boolean actualizarPrecio(String codigo, double nuevoPrecio) {
        var res = coleccion.updateOne(Filters.eq("codigo", codigo), Updates.set("precio", nuevoPrecio));
        return res.getModifiedCount() > 0;
    }

    // ACTIVIDAD 6: Eliminar
    public boolean eliminar(String codigo) {
        var res = coleccion.deleteOne(Filters.eq("codigo", codigo));
        return res.getDeletedCount() > 0;
    }

    // ACTIVIDAD 7: Poco Inventario
    public List<Document> listarPocoInventario(int limite) {
        List<Document> lista = new ArrayList<>();
        coleccion.find(Filters.lt("existencia", limite)).into(lista);
        return lista;
    }

    // RETO 1: Precio mayor a 500
    public List<Document> listarPrecioMayorA500() {
        List<Document> lista = new ArrayList<>();
        coleccion.find(Filters.gt("precio", 500.0)).into(lista);
        return lista;
    }
}
   
