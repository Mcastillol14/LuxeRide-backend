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

    // Relación OneToOne: Una licencia está asociada a un único coche
    // La propiedad mappedBy indica que la relación ya está mapeada en la clase
    // Coche,
    // específicamente en la propiedad "licencia"
    // El fetch = FetchType.EAGER indica que la relación será cargada inmediatamente
    // cuando se consulte la Licencia
    @OneToOne(mappedBy = "licencia", fetch = FetchType.EAGER)
    private Coche coche;
}
