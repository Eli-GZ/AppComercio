package com.example.AdmComercio.controller;

import com.example.AdmComercio.model.Producto;
import com.example.AdmComercio.service.IProductoService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductoController {

    @Autowired
    private IProductoService producServ;

    //ENDPOINT para crear un nuevo producto
    @PostMapping("/productos/crear")
    public String createProducto(@RequestBody Producto produc) {
        producServ.saveProducto(produc);
        //mensaje de creacion correcta
        return "El producto fue creado correctamente";
    }

    //ENDPOINT para obtener todos los productos
    @GetMapping("/productos")
    public List<Producto> getProductos() {
        return producServ.getProductos();
    }

    //ENDPOINT para obtener un producto
    @GetMapping("/productos/{codigo_producto}")
    public Producto getProducto(@PathVariable Long codigo_producto) {
        return producServ.findProducto(codigo_producto);
    }

    //ENDPOINT para eliminar un producto
    @DeleteMapping("/productos/eliminar/{codigo_producto}")
    public String deleteProducto(@PathVariable Long codigo_producto) {
        
        //confirmar que existe un producto
        Producto produ = producServ.findProducto(codigo_producto);

        if (produ != null) {
            producServ.deleteProducto(codigo_producto);
            //mensaje de eliminacion correcta
            return "El producto fue eliminado correctamente";
        } else {
            return "**No se encontro el codigo del producto**";
        }

    }

    //ENDPOINT para modificar una producto
    @PutMapping("/productos/editar/{codigo_producto}")
    public Producto editProducto(@PathVariable Long codigo_producto,
            @RequestParam(required = false, name = "nombre") String nombreNuevo,
            @RequestParam(required = false, name = "marca") String marcaNueva,
            @RequestParam(required = false, name = "costo") Double costoNuevo,
            @RequestParam(required = false, name = "cantidad") Double NuevaCantidad_disponible) {
        //Envio id original(para buscar)

        //Envio nuevos datos para modificar
        producServ.editProducto(codigo_producto, nombreNuevo, marcaNueva, costoNuevo, NuevaCantidad_disponible);

        //busco la persoan editada para mostrarla
        Producto produ = producServ.findProducto(codigo_producto);

        return produ;
    }

    @PutMapping("/productos/editar")
    public Producto editProducto(@RequestBody Producto produ) {
        producServ.editProducto(produ);
        return producServ.findProducto(produ.getCodigo_producto());
    }

//***************************************
    //ENDPOINT para obtener todos los productos
    @GetMapping("/productos/falta_stock")
    public List<Producto> faltaStock() {
        List<Producto> productos = producServ.getProductos();

        return productos.stream().filter(p -> p.getCantidad_disponible() <= 5).collect(Collectors.toList());

    }

}
