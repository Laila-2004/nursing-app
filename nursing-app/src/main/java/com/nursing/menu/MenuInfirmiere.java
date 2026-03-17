package com.nursing.menu;

import com.nursing.entity.*;
import com.nursing.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import static com.nursing.menu.ConsoleUtils.*;

@Component
@RequiredArgsConstructor
public class MenuInfirmiere {

    private final ReservationService reservationService;
    private final DisponibiliteService disponibiliteService;
    private final UserService userService;
    private final AuthService authService;

    public void afficher(Scanner sc, User infirmiere) {
        boolean continuer = true;
        while (continuer) {
            titre("👩‍⚕️  MENU INFIRMIÈRE — " + infirmiere.getNom());
            System.out.println("  1. Consulter mon planning complet");
            System.out.println("  2. Gérer les demandes en attente");
            System.out.println("  3. Gérer mes disponibilités");
            System.out.println("  4. Modifier mon profil");
            System.out.println("  0. Déconnexion");
            ligne();

            String choix = lire(sc, "Votre choix :");
            switch (choix) {
                case "1" -> consulterPlanning(sc, infirmiere);
                case "2" -> gererDemandesEnAttente(sc, infirmiere);
                case "3" -> gererDisponibilites(sc, infirmiere);
                case "4" -> modifierProfil(sc, infirmiere);
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
    private void consulterPlanning(Scanner sc, User infirmiere) {
        titre("📅  MON PLANNING COMPLET");
        List<Reservation> reservations = reservationService.getReservationsInfirmiere(infirmiere);

        if (reservations.isEmpty()) {
            info("Aucune réservation dans votre planning.");
        } else {
            System.out.println();
            reservations.forEach(r -> {
                String couleur = switch (r.getStatut()) {
                    case CONFIRMEE  -> GREEN;
                    case REFUSEE    -> RED;
                    case EN_ATTENTE -> YELLOW;
                };
                System.out.printf("  %s[%d]%s %s à %s — Patient : %-20s | Motif : %-25s | %sStatut : %s%s%n",
                        BOLD, r.getId(), RESET,
                        r.getDate(), r.getHeure(),
                        r.getPatient().getNom(),
                        r.getMotif() != null ? r.getMotif() : "N/A",
                        couleur, r.getStatut(), RESET);
            });
        }
        pauseEntree(sc);
    }

    // ──────────────────────────────────────────────────────────────────────
    private void gererDemandesEnAttente(Scanner sc, User infirmiere) {
        titre("📬  DEMANDES EN ATTENTE");
        List<Reservation> enAttente = reservationService.getReservationsEnAttente(infirmiere);

        if (enAttente.isEmpty()) {
            succes("Aucune demande en attente. Vous êtes à jour !");
            pauseEntree(sc);
            return;
        }

        enAttente.forEach(r ->
                System.out.printf("  %s[%d]%s %s à %s — Patient : %s | Motif : %s%n",
                        BOLD, r.getId(), RESET,
                        r.getDate(), r.getHeure(),
                        r.getPatient().getNom(),
                        r.getMotif() != null ? r.getMotif() : "N/A"));

        System.out.println();
        int id = lireInt(sc, "ID de la réservation à traiter (0 = annuler) :");
        if (id == 0) return;

        System.out.println("  1. Accepter   2. Refuser   0. Annuler");
        String decision = lire(sc, "Votre décision :");

        StatutReservation nouveau = switch (decision) {
            case "1" -> StatutReservation.CONFIRMEE;
            case "2" -> StatutReservation.REFUSEE;
            default  -> null;
        };

        if (nouveau == null) {
            info("Opération annulée.");
            pauseEntree(sc);
            return;
        }

        boolean ok = reservationService.changerStatut((long) id, infirmiere, nouveau);
        if (ok) {
            succes("Réservation [" + id + "] mise à jour → " + nouveau);
        } else {
            erreur("Impossible de mettre à jour cette réservation (ID introuvable ou accès refusé).");
        }
        pauseEntree(sc);
    }

    // ──────────────────────────────────────────────────────────────────────
    private void gererDisponibilites(Scanner sc, User infirmiere) {
        boolean continuer = true;
        while (continuer) {
            titre("🗓️   MES DISPONIBILITÉS");

            List<Disponibilite> dispos = disponibiliteService.getDisponibilitesInfirmiere(infirmiere);
            if (dispos.isEmpty()) {
                info("Aucune disponibilité renseignée.");
            } else {
                dispos.forEach(d -> System.out.println("  " + d));
            }

            System.out.println();
            System.out.println("  1. Ajouter une disponibilité");
            System.out.println("  2. Supprimer une disponibilité");
            System.out.println("  0. Retour");
            ligne();

            String choix = lire(sc, "Votre choix :");
            switch (choix) {
                case "1" -> ajouterDisponibilite(sc, infirmiere);
                case "2" -> supprimerDisponibilite(sc, infirmiere);
                case "0" -> continuer = false;
                default  -> erreur("Choix invalide.");
            }
        }
    }

    private void ajouterDisponibilite(Scanner sc, User infirmiere) {
        titre("➕  AJOUTER UNE DISPONIBILITÉ");

        // Choisir le jour
        System.out.println("  1.Lundi  2.Mardi  3.Mercredi  4.Jeudi  5.Vendredi  6.Samedi  7.Dimanche");
        int jourIdx = lireInt(sc, "Jour :");
        DayOfWeek jour = switch (jourIdx) {
            case 1 -> DayOfWeek.MONDAY;
            case 2 -> DayOfWeek.TUESDAY;
            case 3 -> DayOfWeek.WEDNESDAY;
            case 4 -> DayOfWeek.THURSDAY;
            case 5 -> DayOfWeek.FRIDAY;
            case 6 -> DayOfWeek.SATURDAY;
            case 7 -> DayOfWeek.SUNDAY;
            default -> null;
        };
        if (jour == null) { erreur("Jour invalide."); return; }

        // Heure début
        LocalTime debut = null;
        while (debut == null) {
            try { debut = LocalTime.parse(lire(sc, "Heure début (HH:MM) :")); }
            catch (DateTimeParseException e) { erreur("Format invalide. Exemple : 08:00"); }
        }

        // Heure fin
        LocalTime fin = null;
        while (fin == null) {
            try { fin = LocalTime.parse(lire(sc, "Heure fin   (HH:MM) :")); }
            catch (DateTimeParseException e) { erreur("Format invalide. Exemple : 17:00"); }
        }

        try {
            Disponibilite d = disponibiliteService.ajouterDisponibilite(infirmiere, jour, debut, fin);
            succes("Disponibilité ajoutée : " + d);
        } catch (IllegalArgumentException e) {
            erreur(e.getMessage());
        }
        pauseEntree(sc);
    }

    private void supprimerDisponibilite(Scanner sc, User infirmiere) {
        titre("🗑️   SUPPRIMER UNE DISPONIBILITÉ");
        List<Disponibilite> dispos = disponibiliteService.getDisponibilitesInfirmiere(infirmiere);
        if (dispos.isEmpty()) {
            info("Aucune disponibilité à supprimer.");
            pauseEntree(sc);
            return;
        }
        dispos.forEach(d -> System.out.println("  " + d));
        int id = lireInt(sc, "ID de la disponibilité à supprimer (0 = annuler) :");
        if (id == 0) return;

        boolean ok = disponibiliteService.supprimerDisponibilite((long) id, infirmiere);
        if (ok) succes("Disponibilité supprimée.");
        else    erreur("Impossible de supprimer (ID introuvable ou accès refusé).");
        pauseEntree(sc);
    }

    // ──────────────────────────────────────────────────────────────────────
    private void modifierProfil(Scanner sc, User infirmiere) {
        titre("✏️   MODIFIER MON PROFIL");
        System.out.println("  Nom actuel   : " + infirmiere.getNom());
        System.out.println("  Ville actuelle : " + (infirmiere.getVille() != null ? infirmiere.getVille() : "N/A"));
        System.out.println("  (Laissez vide pour ne pas modifier)");

        String nouveauNom   = lire(sc, "Nouveau nom :");
        String nouvelleVille = lire(sc, "Nouvelle ville :");

        User updated = userService.mettreAJourProfil(infirmiere, nouveauNom, nouvelleVille);
        infirmiere.setNom(updated.getNom());
        infirmiere.setVille(updated.getVille());

        succes("Profil mis à jour !");
        System.out.println("  Nom   : " + infirmiere.getNom());
        System.out.println("  Ville : " + infirmiere.getVille());
        pauseEntree(sc);
    }
}
