package com.luxeride.taxistfg.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "viajes")
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "hora_inicio", nullable = false)
    private LocalDateTime horaInicio;

    @Column(name = "hora_llegada", nullable = false)
    private LocalDateTime horaLlegada;

    @Column(nullable = false)
    private String origen;

    @Column(nullable = false)
    private String destino;

    @Column(name = "distancia_km", nullable = false, precision = 10, scale = 2)
    private BigDecimal distanciaKm;

    @Column(name = "precio_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;

    // Relación ManyToOne con la entidad Usuario (cliente)
    // Cada viaje tiene un cliente asociado, por lo que se define un campo cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    // Relación ManyToOne con la entidad Usuario (taxista)
    // Cada viaje tiene un taxista asociado
    @ManyToOne
    @JoinColumn(name = "taxista_id", nullable = false)
    private Usuario taxista;

    // Relación ManyToOne con la entidad Servicio
    // Cada viaje está asociado con un servicio
    @ManyToOne
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    // Relación ManyToOne con la entidad Coche
    // Cada viaje está asociado a un coche específico
    @ManyToOne
    @JoinColumn(name = "coche_id", nullable = false)
    private Coche coche;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] foto;
}
