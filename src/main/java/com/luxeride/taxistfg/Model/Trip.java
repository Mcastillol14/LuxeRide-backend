package com.luxeride.taxistfg.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", referencedColumnName = "ID", nullable = false)
    private User driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_license_plate", referencedColumnName = "licensePlate", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", referencedColumnName = "ID", nullable = false)
    private Service service;

    @Column(name = "departure_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date departureTime;

    @Column(name = "arrival_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrivalTime;

    @Column(name = "departure_point", nullable = false)
    private String departurePoint;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "distance_km", nullable = false)
    private double distanceKm;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Column(name = "pdf_url")
    private String pdfUrl;
}

