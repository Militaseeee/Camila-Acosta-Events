package com.events_cav.events_venues.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "venues")
@Data
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los atributos
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremental
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

}
