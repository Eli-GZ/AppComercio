package com.example.AdmComercio.repository;

import com.example.AdmComercio.model.Venta;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByFechaVenta(LocalDate fecha_venta);
}
