package com.nursing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilites")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Disponibilite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "infirmiere_id", nullable = false)
    private User infirmiere;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek jour;

    @Column(nullable = false)
    private LocalTime heureDebut;

    @Column(nullable = false)
    private LocalTime heureFin;

    public String getJourFr() {
        return switch (jour) {
            case MONDAY -> "Lundi";
            case TUESDAY -> "Mardi";
            case WEDNESDAY -> "Mercredi";
            case THURSDAY -> "Jeudi";
            case FRIDAY -> "Vendredi";
            case SATURDAY -> "Samedi";
            case SUNDAY -> "Dimanche";
        };
    }

    @Override
    public String toString() {
        return String.format("[%d] %s : %s → %s",
                id, getJourFr(),
                heureDebut.toString(),
                heureFin.toString());
    }
}
