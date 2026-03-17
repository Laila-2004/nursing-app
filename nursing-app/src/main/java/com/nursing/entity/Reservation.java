package com.nursing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "infirmiere_id", nullable = false)
    private User infirmiere;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime heure;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutReservation statut;

    private String motif;

    @Override
    public String toString() {
        return String.format("[%d] %s à %s avec %s | Motif: %s | Statut: %s",
                id,
                date.toString(),
                heure.toString(),
                infirmiere.getNom(),
                motif != null ? motif : "N/A",
                statut.name());
    }
}
