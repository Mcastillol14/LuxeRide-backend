package com.luxeride.taxistfg.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "driver_car")
public class DriverCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "ID", nullable = false)
    private User driver;

    @ManyToOne
    @JoinColumn(name = "car_license_plate", referencedColumnName = "licensePlate", nullable = false)
    private Car car;

    @Column(name = "is_current", nullable = false)
    private boolean isCurrent;
}

