package com.luxeride.taxistfg.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @Column(name = "licensePlate", nullable = false, unique = true)
    private String licensePlate;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "availability", nullable = false)
    private boolean availability;

    @OneToOne
    @JoinColumn(name = "id_license", referencedColumnName = "ID", nullable = false)
    private License license;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "ID")
    private User user;
}

