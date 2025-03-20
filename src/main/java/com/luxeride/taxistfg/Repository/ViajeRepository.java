package com.luxeride.taxistfg.Repository;

import com.luxeride.taxistfg.Model.Viaje;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ViajeRepository extends JpaRepository<Viaje, Integer> {

    @Query(value = "SELECT v FROM Viaje v WHERE v.cliente.id = :idCliente ORDER BY v.id DESC")
    List<Viaje> findViajesCliente(@Param("idCliente") Integer idCliente);


}

