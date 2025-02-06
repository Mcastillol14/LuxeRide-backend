package com.luxeride.taxistfg.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.luxeride.taxistfg.Model.Licencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface LicenciaRepository extends JpaRepository<Licencia, Integer> {
    Optional<Licencia> findByNumero(String numero);
    
    Page<Licencia> findByNumeroContainingIgnoreCase(String numero, Pageable pageable);
    
    Page<Licencia> findAll(Pageable pageable);
}