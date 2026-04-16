package com.rjma.repository;

import com.rjma.entity.FacturaLinea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacturaLineaRepository extends JpaRepository<FacturaLinea, Long> {

    List<FacturaLinea> findByFacturaId(Long facturaId);
}
