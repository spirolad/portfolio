package fr.spirolad.application.port.outbound;

import fr.spirolad.domain.model.Experience;

import java.util.List;
import java.util.Optional;

public interface ExperiencePersistencePort {

    List<Experience> findAll();

    Experience save(Experience experience);

    void deleteById(Long id);

    Optional<Experience> findById(Long id);
}