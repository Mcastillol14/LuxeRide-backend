package com.luxeride.taxistfg.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import java.util.Set;

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

    // Columna dni: DNI único para cada usuario, con validación personalizada
    @Column(nullable = false, unique = true)
    @DniValido(message = "Por favor, proporcione un DNI válido")
    private String dni;

    // Columna email: Dirección de correo electrónico única, con validación de formato de email
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

    // Relación ManyToMany entre Usuario y Coche
    // La tabla intermedia usuario_coche maneja la relación entre estos dos
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuario_coche",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "coche_id"))
    @JsonIgnoreProperties({"usuarios", "licencia", "viajes"})
    private Set<Coche> coches;

    // Relación OneToMany entre Usuario y Viaje como cliente
    // Un usuario puede tener varios viajes como cliente
    @OneToMany(mappedBy = "cliente")
    private Set<Viaje> viajesComoCliente;

    // Relación OneToMany entre Usuario y Viaje como taxista
    // Un usuario puede tener varios viajes como taxista
    @OneToMany(mappedBy = "taxista")
    private Set<Viaje> viajesComoTaxista;

    // Implementación de los métodos de UserDetails para la autenticación
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Asigna el rol del usuario como una autoridad
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.rol.name()));
    }

    @Override
    public String getUsername() {
        // El nombre de usuario es el correo electrónico
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // La cuenta nunca expira
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Indica si la cuenta está bloqueada o no
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Las credenciales nunca expiran
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Indica si la cuenta está habilitada
        return true;
    }
}
