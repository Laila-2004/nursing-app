package com.nursing.repository;

import com.nursing.entity.Reservation;
import com.nursing.entity.StatutReservation;
import com.nursing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByPatient(User patient);

    List<Reservation> findByInfirmiere(User infirmiere);

    List<Reservation> findByInfirmiereAndStatut(User infirmiere, StatutReservation statut);

    List<Reservation> findByPatientOrderByDateAscHeureAsc(User patient);

    List<Reservation> findByInfirmiereOrderByDateAscHeureAsc(User infirmiere);

    boolean existsByInfirmiereAndDateAndHeure(User infirmiere, LocalDate date, LocalTime heure);
}
