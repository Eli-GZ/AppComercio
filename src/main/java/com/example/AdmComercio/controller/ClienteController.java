package com.example.AdmComercio.controller;

import com.example.AdmComercio.model.Cliente;
import com.example.AdmComercio.service.IClienteService;
import java.util.List;
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
public class ClienteController {

    @Autowired
    private IClienteService clientServ;

    //ENDPOINT para crear un nuevo cliente
    @PostMapping("/clientes/crear")
    public String createCliente(@RequestBody Cliente clien) {
        clientServ.saveCliente(clien);
        //mensaje de creacion correcta
        return "El cliente fue creado correctamente";
    }

    //ENDPOINT para obtener todos los clientes
    @GetMapping("/clientes")
    public List<Cliente> getClientes() {
        return clientServ.getClientes();
    }

    //ENDPOINT para obtener un cliente
    @GetMapping("/clientes/{id_cliente}")
    public Cliente getCliente(@PathVariable Long id_cliente) {
        return clientServ.findCliente(id_cliente);
    }

    //ENDPOINT para eliminar un cliente
    @DeleteMapping("/clientes/eliminar/{id_cliente}")
    public String deleteCliente(@PathVariable Long id_cliente) {   
        
        //confirmar que existe un cliente        
        Cliente clien = clientServ.findCliente(id_cliente);

        if (clien != null) {
             clientServ.deleteCliente(id_cliente);
            //mensaje de eliminacion correcta
            return "El cliente fue eliminado correctamente";
        } else {
            return "No se encontro el id del cliente";
        }
    }

    //ENDPOINT para modificar un nuevo cliente
    @PutMapping("/clientes/editar/{id_cliente}")
    public Cliente editCliente(@PathVariable Long id_cliente,
            @RequestParam(required = false, name = "nombre") String nombreNuevo,
            @RequestParam(required = false, name = "apellido") String apellidoNuevo,
            @RequestParam(required = false, name = "dni") String dniNuevo) {
        //Envio id original(para buscar)

        //Envio nuevos datos para modificar
        clientServ.editCliente(id_cliente, nombreNuevo, apellidoNuevo, dniNuevo);

        //busco el cliente editado para mostrarlo
        Cliente clien = clientServ.findCliente(id_cliente);

        return clien;
    }

    @PutMapping("/clientes/editar")
    public Cliente editCliente(@RequestBody Cliente clien) {
        clientServ.editCliente(clien);
        return clientServ.findCliente(clien.getId_cliente());
    }
}
