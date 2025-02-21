package com.luxeride.taxistfg.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String tipo;

    @Column(nullable = false)
    private String descripcion;

    // Uso de BigDecimal para manejar precisión en valores monetarios
    // precision = 10, scale = 2 significa que el precio puede tener hasta 10
    // dígitos,
    // con 2 dígitos después del punto decimal
    @Column(name = "precio_por_km", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPorKm;

    @Column(nullable = false)
    private boolean estado = true;

    // Relación OneToMany: Un servicio puede tener múltiples viajes asociados
    // mappedBy = "servicio" indica que la relación está mapeada en la clase Viaje
    // Es decir, la tabla de viajes tendrá una columna que hace referencia a este
    // servicio
    @OneToMany(mappedBy = "servicio")
    private Set<Viaje> viajes;
}
