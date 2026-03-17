package com.nursing;

import com.nursing.entity.Role;
import com.nursing.entity.User;
import com.nursing.menu.MenuAuth;
import com.nursing.menu.MenuInfirmiere;
import com.nursing.menu.MenuPatient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;

import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
@Order(2)
@RequiredArgsConstructor
public class NursingApplication implements CommandLineRunner {

    private final MenuAuth       menuAuth;
    private final MenuPatient    menuPatient;
    private final MenuInfirmiere menuInfirmiere;

    public static void main(String[] args) {
        SpringApplication.run(NursingApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner sc = new Scanner(System.in);

        boolean appActive = true;
        while (appActive) {
            Optional<User> utilisateur = menuAuth.afficher(sc);

            if (utilisateur.isEmpty()) {
                // L'utilisateur a choisi de quitter depuis l'écran de connexion
                System.out.println("\n  Au revoir ! 👋\n");
                appActive = false;
            } else {
                User user = utilisateur.get();
                if (user.getRole() == Role.PATIENT) {
                    menuPatient.afficher(sc, user);
                } else if (user.getRole() == Role.INFIRMIERE) {
                    menuInfirmiere.afficher(sc, user);
                }
                // Après déconnexion, on retourne à l'écran de connexion
            }
        }

        sc.close();
    }
}
