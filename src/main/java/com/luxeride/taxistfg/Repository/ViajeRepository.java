package com.luxeride.taxistfg.Repository;

import com.luxeride.taxistfg.Model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public interface ViajeRepository extends JpaRepository<Viaje, Integer> {

    Page<Viaje> findAll(Pageable pageable);

    Page<Viaje> findByCliente_Id(Integer clienteId, Pageable pageable);

    Page<Viaje> findByTaxista_Id(Integer taxistaId, Pageable pageable);

    Page<Viaje> findByCoche_Id(Integer cocheId, Pageable pageable);

    Page<Viaje> findByServicio_Id(Integer servicioId, Pageable pageable);

    Page<Viaje> findByOrigenContainingIgnoreCase(String origen, Pageable pageable);

    Page<Viaje> findByDestinoContainingIgnoreCase(String destino, Pageable pageable);

    Page<Viaje> findByHoraInicioBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
}
