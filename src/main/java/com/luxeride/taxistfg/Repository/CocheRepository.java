package com.luxeride.taxistfg.Repository;

import com.luxeride.taxistfg.Model.Coche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CocheRepository extends JpaRepository<Coche, Integer> {

    Optional<Coche> findByMatricula(String matricula);

    Optional<Coche> findByLicenciaId(Integer licenciaId);

    @Query("SELECT c FROM Coche c WHERE c.matricula LIKE  %:matricula%")
    Page<Coche> buscarCochesPorMatricula(@Param("matricula") String matricula, Pageable pageable);

    Page<Coche> findAll(Pageable pageable);

    void deleteById(Integer id);
}