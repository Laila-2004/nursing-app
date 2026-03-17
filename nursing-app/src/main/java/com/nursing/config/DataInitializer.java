package com.nursing.config;

import com.nursing.entity.*;
import com.nursing.repository.DisponibiliteRepository;
import com.nursing.repository.ReservationRepository;
import com.nursing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
@Order(1)
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DisponibiliteRepository disponibiliteRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public void run(String... args) {

        // ── Patients ──────────────────────────────────────────────
        User p1 = userRepository.save(User.builder()
                .nom("Ahmed Benali")
                .email("ahmed@patient.ma")
                .motDePasse("1234")
                .role(Role.PATIENT)
                .ville("Casablanca")
                .build());

        User p2 = userRepository.save(User.builder()
                .nom("Fatima Zahra")
                .email("fatima@patient.ma")
                .motDePasse("1234")
                .role(Role.PATIENT)
                .ville("Rabat")
                .build());

        // ── Infirmières ───────────────────────────────────────────
        User i1 = userRepository.save(User.builder()
                .nom("Nadia Chaoui")
                .email("nadia@infirmiere.ma")
                .motDePasse("1234")
                .role(Role.INFIRMIERE)
                .ville("Casablanca")
                .build());

        User i2 = userRepository.save(User.builder()
                .nom("Sanaa Idrissi")
                .email("sanaa@infirmiere.ma")
                .motDePasse("1234")
                .role(Role.INFIRMIERE)
                .ville("Casablanca")
                .build());

        User i3 = userRepository.save(User.builder()
                .nom("Khadija Moussaoui")
                .email("khadija@infirmiere.ma")
                .motDePasse("1234")
                .role(Role.INFIRMIERE)
                .ville("Rabat")
                .build());

        // ── Disponibilités ────────────────────────────────────────
        // Nadia : Lundi, Mercredi, Vendredi
        disponibiliteRepository.save(Disponibilite.builder().infirmiere(i1)
                .jour(DayOfWeek.MONDAY).heureDebut(LocalTime.of(8, 0)).heureFin(LocalTime.of(12, 0)).build());
        disponibiliteRepository.save(Disponibilite.builder().infirmiere(i1)
                .jour(DayOfWeek.WEDNESDAY).heureDebut(LocalTime.of(9, 0)).heureFin(LocalTime.of(17, 0)).build());
        disponibiliteRepository.save(Disponibilite.builder().infirmiere(i1)
                .jour(DayOfWeek.FRIDAY).heureDebut(LocalTime.of(8, 0)).heureFin(LocalTime.of(14, 0)).build());

        // Sanaa : Mardi, Jeudi
        disponibiliteRepository.save(Disponibilite.builder().infirmiere(i2)
                .jour(DayOfWeek.TUESDAY).heureDebut(LocalTime.of(7, 0)).heureFin(LocalTime.of(15, 0)).build());
        disponibiliteRepository.save(Disponibilite.builder().infirmiere(i2)
                .jour(DayOfWeek.THURSDAY).heureDebut(LocalTime.of(10, 0)).heureFin(LocalTime.of(18, 0)).build());

        // Khadija : Lundi à Jeudi
        disponibiliteRepository.save(Disponibilite.builder().infirmiere(i3)
                .jour(DayOfWeek.MONDAY).heureDebut(LocalTime.of(8, 0)).heureFin(LocalTime.of(16, 0)).build());
        disponibiliteRepository.save(Disponibilite.builder().infirmiere(i3)
                .jour(DayOfWeek.TUESDAY).heureDebut(LocalTime.of(8, 0)).heureFin(LocalTime.of(16, 0)).build());
        disponibiliteRepository.save(Disponibilite.builder().infirmiere(i3)
                .jour(DayOfWeek.WEDNESDAY).heureDebut(LocalTime.of(8, 0)).heureFin(LocalTime.of(16, 0)).build());
        disponibiliteRepository.save(Disponibilite.builder().infirmiere(i3)
                .jour(DayOfWeek.THURSDAY).heureDebut(LocalTime.of(8, 0)).heureFin(LocalTime.of(16, 0)).build());

        // ── Réservations exemple ──────────────────────────────────
        reservationRepository.save(Reservation.builder()
                .patient(p1).infirmiere(i1)
                .date(LocalDate.now().plusDays(2))
                .heure(LocalTime.of(9, 0))
                .motif("Prise de sang")
                .statut(StatutReservation.EN_ATTENTE)
                .build());

        reservationRepository.save(Reservation.builder()
                .patient(p1).infirmiere(i2)
                .date(LocalDate.now().plusDays(5))
                .heure(LocalTime.of(10, 30))
                .motif("Injection")
                .statut(StatutReservation.CONFIRMEE)
                .build());

        reservationRepository.save(Reservation.builder()
                .patient(p2).infirmiere(i3)
                .date(LocalDate.now().plusDays(1))
                .heure(LocalTime.of(8, 0))
                .motif("Soins post-opératoires")
                .statut(StatutReservation.EN_ATTENTE)
                .build());
    }
}
