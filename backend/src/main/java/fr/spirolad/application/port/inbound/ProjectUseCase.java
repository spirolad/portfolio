package fr.spirolad.application.port.inbound;

import fr.spirolad.domain.model.Project;

import java.util.List;

public interface ProjectUseCase {

    List<Project> getAllProjects();

    Project saveProject(Project project);

    void deleteProject(Long id);

    Project getProject(Long id);

    Project updateProject(Long id, Project project);
}