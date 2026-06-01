package fr.spirolad.application.port.outbound;

import fr.spirolad.domain.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectPersistencePort {

    List<Project> findAll();

    Project save(Project project);

    void deleteById(Long id);

    Optional<Project> findById(Long id);
}