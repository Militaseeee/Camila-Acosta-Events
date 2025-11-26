package com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "venues")
@Data
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los atributos
public class VenueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremental
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Integer capacity;

    // -> Un Venue tiene muchos Eventos
    // OJO: Yo debo excluir la colección de los métodos de Lombok para prevenir errores de recursión infinita (StackOverflowError) cuando JPA intenta cargar entidades relacionadas
    @OneToMany(
            mappedBy = "venue",
            cascade = CascadeType.ALL, // Ayuda poner las operaciones CRUD
            fetch = FetchType.LAZY, // Carga perezosa (->optimización N+1)
            orphanRemoval = true // Elimina los eventos si se desasocian
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<EventEntity> events = new ArrayList<>();

    // Metodo de ayuda manual
    // Este metodosirve para la relación bidireccional se manteng al agregar un Evento, y es bueno para un buen manejo del ciclo de vida de las entidades po parte del jpa
    public void addEvent(EventEntity event) {
        if (this.events == null) {
            this.events = new ArrayList<>();
        }
        this.events.add(event);
        event.setVenue(this); // Asegura la referencia inversa
    }
}
