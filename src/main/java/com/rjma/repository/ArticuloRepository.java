package com.rjma.repository;

import com.rjma.entity.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticuloRepository extends JpaRepository<Articulo, Long> {

    List<Articulo> findByNombreContainingIgnoreCase(String nombre);

    List<Articulo> findByCodigoInternoContainingIgnoreCase(String codigoInterno);

    List<Articulo> findByCodigoBarrasContainingIgnoreCase(String codigoBarras);

    List<Articulo> findByActivoTrue();
}
