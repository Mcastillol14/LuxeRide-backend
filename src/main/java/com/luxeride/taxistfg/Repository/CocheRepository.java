package com.luxeride.taxistfg.Repository;

import com.luxeride.taxistfg.Model.Coche;
import com.luxeride.taxistfg.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CocheRepository extends JpaRepository<Coche, Integer> {

    Optional<Coche> findByMatricula(String matricula);

    @Query("SELECT c FROM Coche c WHERE c.matricula LIKE  %:matricula%")
    Page<Coche> buscarCochesPorMatricula(@Param("matricula") String matricula, Pageable pageable);

    List<Coche> findByDisponibleTrue();

    List<Coche> findByDisponibleFalse();

    Page<Coche> findAll(Pageable pageable);

    @Query("SELECT c FROM Coche c JOIN c.usuarios u WHERE u = :taxista AND c.disponible = true AND c.enServicio = false")
    List<Coche> fnindCochesDeTaxista(@Param("taxista") Usuario taxista);;

    List<Coche> findByEnServicioTrue();


    void deleteById(Integer id);
}