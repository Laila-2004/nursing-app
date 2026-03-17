package com.nursing.menu;

import com.nursing.entity.*;
import com.nursing.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static com.nursing.menu.ConsoleUtils.*;

@Component
@RequiredArgsConstructor
public class MenuPatient {

    private final UserService userService;
    private final ReservationService reservationService;
    private final DisponibiliteService disponibiliteService;
    private final AuthService authService;

    public void afficher(Scanner sc, User patient) {
        boolean continuer = true;
        while (continuer) {
            titre("👤  MENU PATIENT — " + patient.getNom());
            System.out.println("  1. Rechercher des infirmières disponibles");
            System.out.println("  2. Réserver un créneau");
            System.out.println("  3. Voir mes réservations");
            System.out.println("  4. Modifier mon profil");
            System.out.println("  0. Déconnexion");
            ligne();

            String choix = lire(sc, "Votre choix :");
            switch (choix) {
                case "1" -> rechercherInfirmieres(sc);
                case "2" -> reserverCreneau(sc, patient);
                case "3" -> voirReservations(sc, patient);
                case "4" -> modifierProfil(sc, patient);
                case "0" -> {
                    authService.deconnecter();
                    succes("Déconnexion réussie.");
                    continuer = false;
                }
                default -> erreur("Choix invalide, réessayez.");
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────────
    private void rechercherInfirmieres(Scanner sc) {
        titre("🔍  RECHERCHER DES INFIRMIÈRES");

        System.out.println("  1. Toutes les infirmières");
        System.out.println("  2. Filtrer par ville");
        String choix = lire(sc, "Choix :");

        List<User> infirmieres;
        if ("2".equals(choix)) {
            String ville = lire(sc, "Ville :");
            infirmieres = userService.listerInfirmieresByVille(ville);
        } else {
            infirmieres = userService.listerInfirmieres();
        }

        if (infirmieres.isEmpty()) {
            info("Aucune infirmière trouvée.");
        } else {
            System.out.println();
            infirmieres.forEach(i -> {
                System.out.println(BOLD + "  " + i + RESET);
                List<Disponibilite> dispos = disponibiliteService.getDisponibilitesInfirmiere(i);
                if (dispos.isEmpty()) {
                    System.out.println("     └─ Aucune disponibilité renseignée");
                } else {
                    dispos.forEach(d -> System.out.println("     └─ " + d));
                }
            });
        }
        pauseEntree(sc);
    }

    // ──────────────────────────────────────────────────────────────────────
    private void reserverCreneau(Scanner sc, User patient) {
        titre("📅  RÉSERVER UN CRÉNEAU");

        // 1. Choisir l'infirmière
        List<User> infirmieres = userService.listerInfirmieres();
        if (infirmieres.isEmpty()) {
            info("Aucune infirmière disponible.");
            pauseEntree(sc);
            return;
        }
        System.out.println();
        for (int i = 0; i < infirmieres.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, infirmieres.get(i));
        }
        int idxInf = lireInt(sc, "Numéro de l'infirmière (0 = annuler) :") - 1;
        if (idxInf < 0 || idxInf >= infirmieres.size()) {
            info("Réservation annulée.");
            pauseEntree(sc);
            return;
        }
        User infirmiere = infirmieres.get(idxInf);

        // 2. Afficher les disponibilités de l'infirmière choisie
        System.out.println();
        info("Disponibilités de " + infirmiere.getNom() + " :");
        List<Disponibilite> dispos = disponibiliteService.getDisponibilitesInfirmiere(infirmiere);
        if (dispos.isEmpty()) {
            info("Cette infirmière n'a pas encore renseigné ses disponibilités.");
            pauseEntree(sc);
            return;
        }
        dispos.forEach(d -> System.out.println("  " + d));

        // 3. Saisir la date
        LocalDate date = null;
        while (date == null) {
            String sDate = lire(sc, "Date souhaitée (AAAA-MM-JJ) :");
            try {
                date = LocalDate.parse(sDate);
            } catch (DateTimeParseException e) {
                erreur("Format de date invalide. Exemple : 2025-06-15");
            }
        }

        // 4. Saisir l'heure
        LocalTime heure = null;
        while (heure == null) {
            String sHeure = lire(sc, "Heure souhaitée (HH:MM) :");
            try {
                heure = LocalTime.parse(sHeure);
            } catch (DateTimeParseException e) {
                erreur("Format d'heure invalide. Exemple : 09:30");
            }
        }

        // 5. Motif
        String motif = lire(sc, "Motif de la visite :");

        // 6. Confirmer
        System.out.println();
        System.out.printf("  ➜ Infirmière : %s%n", infirmiere.getNom());
        System.out.printf("  ➜ Date       : %s%n", date);
        System.out.printf("  ➜ Heure      : %s%n", heure);
        System.out.printf("  ➜ Motif      : %s%n", motif);
        String confirm = lire(sc, "Confirmer ? (o/n) :");
        if (!"o".equalsIgnoreCase(confirm)) {
            info("Réservation annulée.");
            pauseEntree(sc);
            return;
        }

        try {
            Reservation res = reservationService.creerReservation(patient, infirmiere, date, heure, motif);
            succes("Réservation créée avec succès ! (ID : " + res.getId() + ") — statut : EN_ATTENTE");
        } catch (Exception e) {
            erreur(e.getMessage());
        }
        pauseEntree(sc);
    }

    // ──────────────────────────────────────────────────────────────────────
    private void voirReservations(Scanner sc, User patient) {
        titre("📋  MES RÉSERVATIONS");
        List<Reservation> reservations = reservationService.getReservationsPatient(patient);
        if (reservations.isEmpty()) {
            info("Vous n'avez aucune réservation.");
        } else {
            reservations.forEach(r -> {
                String couleur = switch (r.getStatut()) {
                    case CONFIRMEE  -> GREEN;
                    case REFUSEE    -> RED;
                    case EN_ATTENTE -> YELLOW;
                };
                System.out.println("  " + couleur + r + RESET);
            });
        }
        pauseEntree(sc);
    }

    // ──────────────────────────────────────────────────────────────────────
    private void modifierProfil(Scanner sc, User patient) {
        titre("✏️   MODIFIER MON PROFIL");
        System.out.println("  Nom actuel  : " + patient.getNom());
        System.out.println("  Ville actuelle : " + (patient.getVille() != null ? patient.getVille() : "N/A"));
        System.out.println("  (Laissez vide pour ne pas modifier)");

        String nouveauNom  = lire(sc, "Nouveau nom :");
        String nouvelleVille = lire(sc, "Nouvelle ville :");

        User updated = userService.mettreAJourProfil(patient, nouveauNom, nouvelleVille);
        patient.setNom(updated.getNom());
        patient.setVille(updated.getVille());

        succes("Profil mis à jour avec succès !");
        System.out.println("  Nom  : " + patient.getNom());
        System.out.println("  Ville : " + patient.getVille());
        pauseEntree(sc);
    }
}
