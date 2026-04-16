package com.rjma.repository;

import com.rjma.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    List<Cliente> findByNombreComercioContainingIgnoreCase(String nombreComercio);

    List<Cliente> findByMunicipioContainingIgnoreCase(String municipio);

    List<Cliente> findByPoblacionContainingIgnoreCase(String poblacion);

    List<Cliente> findByActivoTrue();
}
