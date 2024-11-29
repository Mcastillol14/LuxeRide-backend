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
    private Integer ID;

    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "ID", nullable = false)
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "license_plate", referencedColumnName = "licensePlate", nullable = false)
    private Car car;
}
