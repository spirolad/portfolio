package fr.spirolad.application.port.outbound;

import fr.spirolad.domain.model.Education;

import java.util.List;
import java.util.Optional;

public interface EducationPersistencePort {

    List<Education> findAll();

    Education save(Education education);

    void deleteById(Long id);

    Optional<Education> findById(Long id);
}