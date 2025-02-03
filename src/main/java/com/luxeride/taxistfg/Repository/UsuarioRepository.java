package com.luxeride.taxistfg.Repository;

import com.luxeride.taxistfg.Model.Usuario;
import com.luxeride.taxistfg.Model.Rol;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByDni(String dni);

    Page<Usuario> findAll(Pageable pageable);

    Page<Usuario> findByRol(Rol rol, Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);

    @Query("SELECT u FROM Usuario u WHERE (:rol IS NULL OR u.rol = :rol) AND (:dni IS NULL OR u.dni LIKE %:dni%)")
    Page<Usuario> findByRolAndDni(Pageable pageable, Rol rol, String dni);

    Page<Usuario> findByAccountNonLockedFalse(Pageable pageable);

    Page<Usuario> findByAccountNonLockedTrue(Pageable pageable);

}
