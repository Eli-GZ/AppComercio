package com.example.AdmComercio.service;

import com.example.AdmComercio.model.Cliente;
import com.example.AdmComercio.model.Producto;
import com.example.AdmComercio.model.Venta;
import com.example.AdmComercio.repository.IVentaRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VentaService implements IVentaService {

    @Autowired
    private IVentaRepository VentRepo;

    @Override
    public List<Venta> getVentas() {
        List<Venta> listaVentas = VentRepo.findAll();
        return listaVentas;
    }

    @Override
    public void saveVenta(Venta vent) {
        VentRepo.save(vent);
    }

    @Override
    public void deleteVenta(Long codigo_venta) {
        VentRepo.deleteById(codigo_venta);
    }

    @Override
    public Venta findVenta(Long codigo_venta) {
        Venta vent = VentRepo.findById(codigo_venta).orElse(null);
        return vent;
    }

    @Override
    public void editVenta(Long codigo_venta, LocalDate nuevaFecha_venta, Double nuevoTotal, List<Producto> nuevaListaProductos, Cliente nuevoUnCliente) {

        Venta vent = this.findVenta(codigo_venta);

        vent.setFechaVenta(nuevaFecha_venta);
        vent.setTotal(nuevoTotal);
        vent.setListaProductos(nuevaListaProductos);
        vent.setUnCliente(nuevoUnCliente);

        this.saveVenta(vent);

    }

    @Override
    public void editVenta(Venta vent) {
        this.saveVenta(vent);
    }

    @Override
    public List<Venta> getVentasPorFecha(LocalDate fecha) {
        return VentRepo.findByFechaVenta(fecha);
    }

    @Override
    public List<Venta> getVentasConProducto(Long codigoProducto) {
        return VentRepo.findByProductoCodigo(codigoProducto);
    }

}
