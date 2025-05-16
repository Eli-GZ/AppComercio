package com.example.AdmComercio.service;

import com.example.AdmComercio.model.Cliente;
import com.example.AdmComercio.repository.IClienteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService implements IClienteService {

    @Autowired
    private IClienteRepository ClienRepo;

    @Override
    public List<Cliente> getClientes() {
        List<Cliente> listaClientes = ClienRepo.findAll();
        return listaClientes;
    }

    @Override
    public void saveCliente(Cliente clien) {
        ClienRepo.save(clien);
    }

    @Override
    public void deleteCliente(Long id_cliente) {
        ClienRepo.deleteById(id_cliente);
    }

    @Override
    public Cliente findCliente(Long id_cliente) {
        Cliente clien = ClienRepo.findById(id_cliente).orElse(null);
        return clien;
    }

    @Override
    public void editCliente(Long id_cliente, String nombreNuevo, String apellidoNuevo, String dniNuevo) {
        Cliente clien = this.findCliente(id_cliente);

        clien.setNombre(nombreNuevo);
        clien.setApellido(apellidoNuevo);
        clien.setDni(dniNuevo);

        this.saveCliente(clien);
    }

    @Override
    public void editCliente(Cliente clien) {
        this.saveCliente(clien);
    }

}
