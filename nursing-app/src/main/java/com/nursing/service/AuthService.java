package com.nursing.service;

import com.nursing.entity.User;
import com.nursing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private User utilisateurConnecte;

    /**
     * Tente de connecter un utilisateur via email + mot de passe.
     * @return l'utilisateur si trouvé, sinon Optional.empty()
     */
    public Optional<User> connecter(String email, String motDePasse) {
        Optional<User> user = userRepository.findByEmailAndMotDePasse(email, motDePasse);
        user.ifPresent(u -> this.utilisateurConnecte = u);
        return user;
    }

    public void deconnecter() {
        this.utilisateurConnecte = null;
    }

    public User getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public boolean estConnecte() {
        return utilisateurConnecte != null;
    }
}
