package com.luxeride.taxistfg.Repository;

import com.luxeride.taxistfg.Model.Servicio;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

    Optional<Servicio> findByTipo(String tipo);

    Page<Servicio> findAll(Pageable pageable);

    Page<Servicio> findByEstadoTrue(Pageable pageable);

    Page<Servicio> findByEstadoFalse(Pageable pageable);

    Page<Servicio> findByEstado(Pageable pageable, Boolean estado);

}