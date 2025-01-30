package com.luxeride.taxistfg.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.luxeride.taxistfg.Model.Coche;

public interface CocheRepository extends JpaRepository<Coche, Integer> {

    Optional<Coche> findByLicencia_Id(Integer licenciaId);

    Optional<Coche> findByMatricula(String matricula);

    Page<Coche> findByMarca(String marca, Pageable pageable);

    Page<Coche> findByDisponibleTrue(Pageable pageable);

    Page<Coche> findByEstadoTrue(Pageable pageable);

    Page<Coche> findByEstadoFalse(Pageable pageable);

    Page<Coche> findByMarcaAndModelo(String marca, String modelo, Pageable pageable);

    Page<Coche> findByUsuarios_Id(Integer usuarioId, Pageable pageable);

    Page<Coche> findAll(Pageable pageable);
}
