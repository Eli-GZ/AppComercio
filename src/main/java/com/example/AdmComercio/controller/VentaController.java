package com.example.AdmComercio.controller;

import com.example.AdmComercio.dto.VentaDTO;
import com.example.AdmComercio.model.Cliente;
import com.example.AdmComercio.model.Producto;
import com.example.AdmComercio.model.Venta;
import com.example.AdmComercio.service.IClienteService;
import com.example.AdmComercio.service.IProductoService;
import com.example.AdmComercio.service.IVentaService;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VentaController {

    @Autowired
    private IProductoService produServ;
    @Autowired
    private IClienteService clientServ;
    @Autowired
    private IVentaService ventaServ;

//ENDPOINT para crear una nueva venta
    @PostMapping("/ventas/crear")
    public String createVenta(@RequestBody VentaDTO ventaDTO) {

        //obtener todos los productos   
        List<Producto> totalProductos = produServ.getProductos();

        // Filtrar solo las ids
        List<Producto> productosSeleccionados = totalProductos.stream()
                .filter(p -> ventaDTO.getListaProductosIds()
                .contains(p.getCodigo_producto())).collect(Collectors.toList());

        //obtener cliente
        List<Cliente> todosLosClientes = clientServ.getClientes();

        Cliente client = todosLosClientes.stream()
                .filter(c -> c.getId_cliente().equals(ventaDTO.getClienteId()))
                .findFirst()
                .orElse(null);

        if (client == null || productosSeleccionados.isEmpty()) {
            return "** Error: Cliente o productos no encontrados **";
        }

        // Crear la venta
        Venta venta = new Venta();
        venta.setFecha_venta(ventaDTO.getFecha_venta());
        venta.setTotal(ventaDTO.getTotal());
        venta.setListaProductos(productosSeleccionados);
        venta.setUnCliente(client);

        // Guardar la venta
        ventaServ.saveVenta(venta);

//mensaje de creacion correcta
        return "La venta fue creada correctamente";
    }

//ENDPOINT para obtener todas las ventas
    @GetMapping("/ventas")
    public List<Venta> getVentas() {
        return ventaServ.getVentas();
    }

//ENDPOINT para obtener una venta
    @GetMapping("/ventas/{codigo_venta}")
    public Venta getVenta(@PathVariable Long codigo_venta) {
        return ventaServ.findVenta(codigo_venta);
    }

//ENDPOINT para eliminar una venta
    @DeleteMapping("/ventas/eliminar/{codigo_venta}")
    public String deleteVenta(@PathVariable Long codigo_venta) {
        //confirmar que existe un cliente        
        Venta vent = ventaServ.findVenta(codigo_venta);

        if (vent != null) {
            ventaServ.deleteVenta(codigo_venta);
            //mensaje de eliminacion correcta
            return "La venta fue eliminada correctamente";
        } else {
            return "**No se encontro el codigo de venta*";
        }
    }

//ENDPOINT para modificar un venta
    @PutMapping("/ventas/editar/{codigo_venta}")
    public Venta editVenta(@PathVariable Long codigo_venta,
            @RequestParam(required = false, name = "fecha") LocalDate nuevaFecha_venta,
            @RequestParam(required = false, name = "total") Double nuevoTotal,
            @RequestParam(required = false, name = "listaProductos") List<Producto> nuevaListaProductos,
            @RequestParam(required = false, name = "unCLiente") Cliente nuevoUnCliente) {
        //Envio id original(para buscar)

        //Envio nuevos datos para modificar
        ventaServ.editVenta(codigo_venta, nuevaFecha_venta, nuevoTotal, nuevaListaProductos, nuevoUnCliente);

        //busco la venta editada para mostrarla
        Venta vent = ventaServ.findVenta(codigo_venta);

        return vent;
    }

    @PutMapping("/ventas/editar")
    public Venta editVenta(@RequestBody Venta vent) {
        ventaServ.editVenta(vent);
        return ventaServ.findVenta(vent.getCodigo_venta());
    }
//**********************************************

    @GetMapping("/ventas/productos/{codigo_venta}")
    public List<Producto> getProductoVenta(@PathVariable Long codigo_venta) {
        Venta vent = ventaServ.findVenta(codigo_venta);

        if (vent != null) {
            return vent.getListaProductos();
        } else {
            return null;
        }
    }
}
