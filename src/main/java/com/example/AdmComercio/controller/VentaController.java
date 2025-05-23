package com.example.AdmComercio.controller;

import com.example.AdmComercio.dto.TotalVentaDTO;
import com.example.AdmComercio.dto.VentaDTO;
import com.example.AdmComercio.model.Cliente;
import com.example.AdmComercio.model.Producto;
import com.example.AdmComercio.model.Venta;
import com.example.AdmComercio.service.IClienteService;
import com.example.AdmComercio.service.IProductoService;
import com.example.AdmComercio.service.IVentaService;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        venta.setFechaVenta(ventaDTO.getFecha_venta());
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
            return "**No se encontro el codigo de venta**";
        }
    }
//ENDPOINT para editar una venta

    @PutMapping("/ventas/editar/{codigo_venta}")
    public String editVenta(@PathVariable Long codigo_venta, @RequestBody VentaDTO ventaDTO) {

        // Buscar la venta original
        Venta ventaExistente = ventaServ.findVenta(codigo_venta);
        if (ventaExistente == null) {
            return "** Error: Venta no encontrada **";
        }

        // Obtener todos los productos
        List<Producto> todosLosProductos = produServ.getProductos();

        // Filtrar los productos seleccionados
        List<Producto> productosSeleccionados = todosLosProductos.stream()
                .filter(p -> ventaDTO.getListaProductosIds().contains(p.getCodigo_producto()))
                .collect(Collectors.toList());

        // Buscar el cliente
        List<Cliente> todosLosClientes = clientServ.getClientes();
        Cliente cliente = todosLosClientes.stream()
                .filter(c -> c.getId_cliente().equals(ventaDTO.getClienteId()))
                .findFirst()
                .orElse(null);
        
        if (cliente == null || productosSeleccionados.isEmpty()) {
            return "** Error: Cliente o productos no encontrados **";
        }

        // Actualizar los datos de la venta
        ventaExistente.setFechaVenta(ventaDTO.getFecha_venta());
        ventaExistente.setTotal(ventaDTO.getTotal());
        ventaExistente.setListaProductos(productosSeleccionados);
        ventaExistente.setUnCliente(cliente);

        // Guardar los cambios
        ventaServ.saveVenta(ventaExistente);
        
        return "La venta fue editada correctamente";
    }
//**********************************************    

//ENDPOINT para obtener una lista de productos de una venta
    @GetMapping("/ventas/productos/{codigo_venta}")
    public List<Producto> getListaProductos(@PathVariable Long codigo_venta) {

        // Buscar la venta original
        Venta ventaExistente = ventaServ.findVenta(codigo_venta);
        
        if (ventaExistente == null) {
            return null;
        }
        
        return ventaExistente.getListaProductos();
    }

//ENDPOINT para obtener el total de ventas de un dia determinado
    @GetMapping("/ventas/fecha/{fecha_venta}")
    public ResponseEntity<Map<String, Object>> obtenerResumenPorFecha(@PathVariable String fecha_venta) {
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fecha_venta); // Formato "yyyy-MM-dd"
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Formato de fecha no permitido. Usá yyyy-MM-dd"));
        }
        
        List<Venta> ventasDelDia = ventaServ.getVentasPorFecha(fecha);
        //Suma total del dia
        double sumaTotal = ventasDelDia.stream()
                .mapToDouble(v -> v.getTotal() != null ? v.getTotal() : 0.0)
                .sum();
        
        int cantidad = ventasDelDia.size();
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("fecha", fecha);
        respuesta.put("totalVentas", cantidad);
        respuesta.put("montoTotal", sumaTotal);
        
        return ResponseEntity.ok(respuesta);
    }

//ENDPOINT para obtener la mayor venta y datos extra
    @GetMapping("/ventas/mayor_venta")
    public ResponseEntity<?> obtenerVentaMayor() {
        List<Venta> todasVentas = ventaServ.getVentas();
        
        if (todasVentas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay ventas registradas.");
        }
        
        Venta ventaMayor = todasVentas.stream()
                .max(Comparator.comparingDouble(v -> v.getTotal() != null ? v.getTotal() : 0.0))
                .orElse(null);
        
        TotalVentaDTO dto = new TotalVentaDTO();
        dto.setCodigo_venta(ventaMayor.getCodigo_venta());
        dto.setTotal(ventaMayor.getTotal());
        dto.setCantidadProductos(ventaMayor.getListaProductos() != null ? ventaMayor.getListaProductos().size() : 0);
        dto.setNombre(ventaMayor.getUnCliente().getNombre());
        dto.setApellido(ventaMayor.getUnCliente().getApellido());
        
        return ResponseEntity.ok(dto);
    }
}
