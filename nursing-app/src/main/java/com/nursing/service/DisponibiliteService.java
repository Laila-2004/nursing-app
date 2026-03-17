package com.nursing.service;

import com.nursing.entity.Disponibilite;
import com.nursing.entity.User;
import com.nursing.repository.DisponibiliteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DisponibiliteService {

    private final DisponibiliteRepository disponibiliteRepository;

    public List<Disponibilite> getDisponibilitesInfirmiere(User infirmiere) {
        return disponibiliteRepository.findByInfirmiere(infirmiere);
    }

    public List<Disponibilite> getDisponibilitesParJour(User infirmiere, DayOfWeek jour) {
        return disponibiliteRepository.findByInfirmiereAndJour(infirmiere, jour);
    }

    @Transactional
    public Disponibilite ajouterDisponibilite(User infirmiere, DayOfWeek jour,
                                               LocalTime heureDebut, LocalTime heureFin) {
        if (heureDebut.isAfter(heureFin)) {
            throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin.");
        }
        Disponibilite dispo = Disponibilite.builder()
                .infirmiere(infirmiere)
                .jour(jour)
                .heureDebut(heureDebut)
                .heureFin(heureFin)
                .build();
        return disponibiliteRepository.save(dispo);
    }

    @Transactional
    public boolean supprimerDisponibilite(Long id, User infirmiere) {
        Optional<Disponibilite> dispo = disponibiliteRepository.findById(id);
        if (dispo.isPresent() && dispo.get().getInfirmiere().getId().equals(infirmiere.getId())) {
            disponibiliteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
