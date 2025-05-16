
package com.example.AdmComercio.service;

import com.example.AdmComercio.model.Producto;
import java.util.List;


public interface IProductoService {
      
    //Traer todos los productos
    public List<Producto> getProductos();

    //Guardar una producto 
    public void saveProducto(Producto produ);

    //Borrar un producto
    public void deleteProducto(Long codigo_producto);

    //Encontrar un producto
    public Producto findProducto(Long codigo_producto);

    //Editar persona
    public void editProducto(Long codigo_producto, String nombreNuevo, String marcaNueva, Double costoNuevo, Double NuevaCantidad_disponible);

    public void editProducto(Producto produ);
}
