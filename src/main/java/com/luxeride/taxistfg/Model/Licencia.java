package com.luxeride.taxistfg.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "licencias")
public class Licencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private boolean estado = true;

    @OneToOne(mappedBy = "licencia", fetch = FetchType.EAGER)
    private Coche coche;
}
