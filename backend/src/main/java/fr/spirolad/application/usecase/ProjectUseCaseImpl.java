package fr.spirolad.application.usecase;

import fr.spirolad.application.port.inbound.ProjectUseCase;
import fr.spirolad.application.port.outbound.ProjectPersistencePort;
import fr.spirolad.domain.exception.ProjectNotFoundException;
import fr.spirolad.domain.model.Project;

import java.util.List;

public class ProjectUseCaseImpl implements ProjectUseCase {

    private final ProjectPersistencePort projectPersistencePort;

    public ProjectUseCaseImpl(ProjectPersistencePort projectPersistencePort) {
        this.projectPersistencePort = projectPersistencePort;
    }

    @Override
    public List<Project> getAllProjects() {
        return projectPersistencePort.findAll();
    }

    @Override
    public Project saveProject(Project project) {
        return projectPersistencePort.save(project);
    }

    @Override
    public void deleteProject(Long id) {
        ensureProjectExists(id);
        projectPersistencePort.deleteById(id);
    }

    @Override
    public Project getProject(Long id) {
        return projectPersistencePort.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
    }

    @Override
    public Project updateProject(Long id, Project project) {
        Project existingProject = ensureProjectExists(id);
        existingProject.setName(project.getName());
        existingProject.setDescription(project.getDescription());
        existingProject.setLink(project.getLink());
        existingProject.setScreenshots(project.getScreenshots());
        existingProject.setTechnologies(project.getTechnologies());
        return projectPersistencePort.save(existingProject);
    }

    private Project ensureProjectExists(Long id) {
        return projectPersistencePort.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
    }
}