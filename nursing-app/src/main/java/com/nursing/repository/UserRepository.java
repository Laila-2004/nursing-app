package com.nursing.repository;

import com.nursing.entity.Role;
import com.nursing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndMotDePasse(String email, String motDePasse);

    List<User> findByRole(Role role);

    List<User> findByRoleAndVille(Role role, String ville);
}
