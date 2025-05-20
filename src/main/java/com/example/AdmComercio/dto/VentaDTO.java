package com.example.AdmComercio.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VentaDTO {

    private LocalDate fecha_venta;
    private Double total;
    private List<Long> listaProductosIds;
    private Long clienteId;

}
