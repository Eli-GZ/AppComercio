package com.example.AdmComercio.repository;

import com.example.AdmComercio.model.Venta;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IVentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByFechaVenta(LocalDate fecha_venta);

    @Query("SELECT v FROM Venta v JOIN v.listaProductos p WHERE p.codigo_producto = :codigo")
    List<Venta> findByProductoCodigo(@Param("codigo") Long codigoProducto);
}
