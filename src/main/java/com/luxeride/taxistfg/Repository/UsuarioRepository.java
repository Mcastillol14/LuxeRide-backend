package com.luxeride.taxistfg.Repository;

import com.luxeride.taxistfg.Model.Usuario;
import com.luxeride.taxistfg.Model.Rol;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByDni(String dni);

    Page<Usuario> findAll(Pageable pageable);

    Page<Usuario> findByRol(Rol rol, Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);

}
