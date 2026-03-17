package com.nursing.service;

import com.nursing.entity.Reservation;
import com.nursing.entity.StatutReservation;
import com.nursing.entity.User;
import com.nursing.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<Reservation> getReservationsPatient(User patient) {
        return reservationRepository.findByPatientOrderByDateAscHeureAsc(patient);
    }

    public List<Reservation> getReservationsInfirmiere(User infirmiere) {
        return reservationRepository.findByInfirmiereOrderByDateAscHeureAsc(infirmiere);
    }

    public List<Reservation> getReservationsEnAttente(User infirmiere) {
        return reservationRepository.findByInfirmiereAndStatut(infirmiere, StatutReservation.EN_ATTENTE);
    }

    @Transactional
    public Reservation creerReservation(User patient, User infirmiere,
                                         LocalDate date, LocalTime heure, String motif) {
        // Vérifier si le créneau est déjà pris
        boolean dejaPris = reservationRepository.existsByInfirmiereAndDateAndHeure(infirmiere, date, heure);
        if (dejaPris) {
            throw new IllegalStateException("Ce créneau est déjà réservé pour cette infirmière.");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Impossible de réserver une date passée.");
        }

        Reservation reservation = Reservation.builder()
                .patient(patient)
                .infirmiere(infirmiere)
                .date(date)
                .heure(heure)
                .motif(motif)
                .statut(StatutReservation.EN_ATTENTE)
                .build();

        return reservationRepository.save(reservation);
    }

    @Transactional
    public boolean changerStatut(Long reservationId, User infirmiere, StatutReservation nouveauStatut) {
        Optional<Reservation> optRes = reservationRepository.findById(reservationId);
        if (optRes.isEmpty()) return false;

        Reservation res = optRes.get();
        if (!res.getInfirmiere().getId().equals(infirmiere.getId())) return false;

        res.setStatut(nouveauStatut);
        reservationRepository.save(res);
        return true;
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }
}
