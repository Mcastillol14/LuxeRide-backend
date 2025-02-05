package com.luxeride.taxistfg.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.luxeride.taxistfg.Model.Licencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface LicenciaRepository extends JpaRepository<Licencia, Integer> {
    Optional<Licencia> findByNumero(String numero);
    
    @Query("SELECT l FROM Licencia l WHERE (:numero IS NULL OR l.numero LIKE %:numero%)")
    Page<Licencia> findByNumeroContaining(@Param("numero") String numero, Pageable pageable);
    
    Page<Licencia> findAll(Pageable pageable);
}