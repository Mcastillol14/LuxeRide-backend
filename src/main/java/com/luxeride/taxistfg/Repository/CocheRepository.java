package com.luxeride.taxistfg.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.luxeride.taxistfg.Model.Coche;

public interface CocheRepository extends JpaRepository<Coche, Integer> {

}   
