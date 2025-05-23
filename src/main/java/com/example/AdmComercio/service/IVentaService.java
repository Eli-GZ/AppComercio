package com.example.AdmComercio.service;

import com.example.AdmComercio.model.Cliente;
import com.example.AdmComercio.model.Producto;
import com.example.AdmComercio.model.Venta;
import java.time.LocalDate;
import java.util.List;

public interface IVentaService {
    
    //Traer todas las ventas
    public List<Venta> getVentas();

    //Guardar una venta 
    public void saveVenta(Venta vent);

    //Borrar una venta
    public void deleteVenta(Long codigo_venta);

    //Encontrar una venta
    public Venta findVenta(Long codigo_venta);

    //Editar una venta
    public void editVenta(Long codigo_venta, LocalDate nuevaFecha_venta, Double nuevoTotal, List<Producto> nuevaListaProductos, Cliente nuevoUnCliente);

    public void editVenta(Venta vent);
    
    //Traer venta por fecha
    public List<Venta> getVentasPorFecha(LocalDate fecha);
    
    //Traer venta por fecha
    public List<Venta> getVentasConProducto(Long codigoProducto);

}
