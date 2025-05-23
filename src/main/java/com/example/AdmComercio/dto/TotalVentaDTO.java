package com.example.AdmComercio.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalVentaDTO {

    private Long codigo_venta;
    private Double total;
    private int cantidadProductos;
    private Long clienteId;
    private String nombre;
    private String apellido;

}
