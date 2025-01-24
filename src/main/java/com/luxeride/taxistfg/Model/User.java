package com.luxeride.taxistfg.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "DNI", unique = true, nullable = false)
    private String DNI;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Car> cars;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> (GrantedAuthority) () -> "ROLE_" + role).toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User(String name, String lastName, String DNI, String email, String password, Set<String> roles) {
        this.name = name;
        this.lastName = lastName;
        if (validateDNI(DNI)) {
            this.DNI = DNI;
        } else {
            throw new IllegalArgumentException("El formato del DNI no es válido.");
        }
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    private boolean validateDNI(String dni) {
        if (dni == null || !dni.matches("\\d{8}[A-Za-z]")) {
            return false;
        }
        String numPart = dni.substring(0, 8);
        char letterPart = dni.charAt(8);

        int dniNumber = Integer.parseInt(numPart);

        char[] letras = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B',
                'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};

        int index = dniNumber % 23;
        char correctLetter = letras[index];

        return letterPart == correctLetter;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public boolean isDriver() {
        return roles.contains("DRIVER");
    }
}

