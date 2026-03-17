package com.nursing.menu;

import com.nursing.entity.User;
import com.nursing.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

import static com.nursing.menu.ConsoleUtils.*;

@Component
@RequiredArgsConstructor
public class MenuAuth {

    private final AuthService authService;

    /**
     * Affiche l'écran de connexion et retourne l'utilisateur connecté.
     * Boucle jusqu'à connexion réussie ou demande de quitter.
     */
    public Optional<User> afficher(Scanner sc) {
        while (true) {
            titre("🏥  SYSTÈME DE MISE EN RELATION PATIENTS / INFIRMIÈRES");

            System.out.println();
            System.out.println(BOLD + "  Comptes de démonstration :" + RESET);
            System.out.println("  Patient    : ahmed@patient.ma     / 1234");
            System.out.println("  Patient    : fatima@patient.ma    / 1234");
            System.out.println("  Infirmière : nadia@infirmiere.ma  / 1234");
            System.out.println("  Infirmière : sanaa@infirmiere.ma  / 1234");
            System.out.println("  Infirmière : khadija@infirmiere.ma/ 1234");
            System.out.println();
            System.out.println("  Tapez " + YELLOW + "quitter" + RESET + " comme email pour sortir.");
            ligne();

            String email = lire(sc, "Email     :");
            if ("quitter".equalsIgnoreCase(email)) return Optional.empty();

            String mdp = lire(sc, "Mot de passe :");

            Optional<User> user = authService.connecter(email, mdp);
            if (user.isPresent()) {
                succes("Bienvenue, " + user.get().getNom() + " ! (Rôle : " + user.get().getRole() + ")");
                return user;
            } else {
                erreur("Email ou mot de passe incorrect. Réessayez.");
            }
        }
    }
}
