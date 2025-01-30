package com.luxeride.taxistfg.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.luxeride.taxistfg.Model.Licencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LicenciaRepository extends JpaRepository<Licencia, Integer> {

    Optional<Licencia> findByNumero(String numero);

    Page<Licencia> findAll(Pageable pageable);

    Page<Licencia> findByEstadoTrue(Pageable pageable);

    Page<Licencia> findByEstadoFalse(Pageable pageable);
}
