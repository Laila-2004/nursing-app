package com.nursing.service;

import com.nursing.entity.Role;
import com.nursing.entity.User;
import com.nursing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> listerInfirmieres() {
        return userRepository.findByRole(Role.INFIRMIERE);
    }

    public List<User> listerInfirmieresByVille(String ville) {
        return userRepository.findByRoleAndVille(Role.INFIRMIERE, ville);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User mettreAJourProfil(User user, String nouveauNom, String nouvelleVille) {
        if (nouveauNom != null && !nouveauNom.isBlank()) {
            user.setNom(nouveauNom);
        }
        if (nouvelleVille != null && !nouvelleVille.isBlank()) {
            user.setVille(nouvelleVille);
        }
        return userRepository.save(user);
    }

    public User sauvegarder(User user) {
        return userRepository.save(user);
    }
}
