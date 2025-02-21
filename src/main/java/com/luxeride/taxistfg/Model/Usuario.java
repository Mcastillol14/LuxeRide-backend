package com.luxeride.taxistfg.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import com.luxeride.taxistfg.Validation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false, unique = true)
    @DniValido(message = "Por favor, proporcione un DNI válido")
    private String dni;

    @Column(nullable = false, unique = true)
    @Email(message = "Por favor, proporcione una dirección de correo electrónico válida")
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Column(nullable = false)
    private boolean accountNonLocked = true;

    @ManyToMany(mappedBy = "usuarios")
    private Set<Coche> coches = new HashSet<>();

    @OneToMany(mappedBy = "cliente")
    private Set<Viaje> viajesComoCliente;

    @OneToMany(mappedBy = "taxista")
    private Set<Viaje> viajesComoTaxista;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.rol.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) &&
                Objects.equals(dni, usuario.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dni);
    }
}