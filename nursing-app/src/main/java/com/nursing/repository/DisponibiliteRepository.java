package com.nursing.repository;

import com.nursing.entity.Disponibilite;
import com.nursing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Long> {

    List<Disponibilite> findByInfirmiere(User infirmiere);

    List<Disponibilite> findByInfirmiereAndJour(User infirmiere, DayOfWeek jour);

    void deleteByInfirmiere(User infirmiere);
}
