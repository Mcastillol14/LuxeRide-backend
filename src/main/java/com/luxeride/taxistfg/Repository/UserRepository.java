package com.luxeride.taxistfg.Repository;

import com.luxeride.taxistfg.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByDNI(String dni);
    Page<User> findAll(Pageable pageable);
    Page<User> findByRolesContaining(String role, Pageable pageable);
    boolean existsByEmail(String email);
    boolean existsByDNI(String dni);


}
