package com.example.AdmComercio.service;

import com.example.AdmComercio.model.Cliente;
import com.example.AdmComercio.model.Producto;
import com.example.AdmComercio.model.Venta;
import java.time.LocalDate;
import java.util.List;

public interface IVentaService {
    //Traer todos los productos

    public List<Venta> getVentas();

    //Guardar una producto 
    public void saveVenta(Venta vent);

    //Borrar un producto
    public void deleteVenta(Long codigo_venta);

    //Encontrar un producto
    public Venta findVenta(Long codigo_venta);

    //Editar persona
    public void editVenta(Long codigo_venta, LocalDate nuevaFecha_venta, Double nuevoTotal, List<Producto> nuevaListaProductos, Cliente nuevoUnCliente);

    public void editVenta(Venta vent);
}
