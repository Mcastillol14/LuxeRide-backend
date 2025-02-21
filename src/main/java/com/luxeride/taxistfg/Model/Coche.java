package com.luxeride.taxistfg.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coches")
public class Coche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String matricula;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @OneToOne
    @JoinColumn(name = "licencia_id", unique = true)
    private Licencia licencia;

    @Column(nullable = false)
    private boolean disponible = false;

    @Column(nullable = false)
    private boolean estado = true;

    @ManyToMany
    @JoinTable(name = "usuario_coche",
            joinColumns = @JoinColumn(name = "coche_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private Set<Usuario> usuarios = new HashSet<>();

    @OneToMany(mappedBy = "coche")
    private Set<Viaje> viajes;

    public void addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        usuario.getCoches().add(this);
    }

    public void removeUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
        usuario.getCoches().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coche coche = (Coche) o;
        return Objects.equals(id, coche.id) &&
                Objects.equals(matricula, coche.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matricula);
    }
}