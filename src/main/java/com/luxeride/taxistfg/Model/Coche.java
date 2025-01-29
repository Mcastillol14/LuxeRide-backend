package com.luxeride.taxistfg.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Set;

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

    @ManyToMany(mappedBy = "coches")
    private Set<Usuario> usuarios;

    @OneToMany(mappedBy = "coche")
    private Set<Viaje> viajes;
}