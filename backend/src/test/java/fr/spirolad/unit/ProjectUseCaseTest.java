package fr.spirolad.unit;

import fr.spirolad.application.port.outbound.ProjectPersistencePort;
import fr.spirolad.application.usecase.ProjectUseCaseImpl;
import fr.spirolad.domain.exception.ProjectNotFoundException;
import fr.spirolad.domain.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProjectUseCaseTest {

    private ProjectPersistencePort projectPersistencePort;
    private ProjectUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        projectPersistencePort = mock(ProjectPersistencePort.class);
        useCase = new ProjectUseCaseImpl(projectPersistencePort);
    }

    @Test
    void getAllProjects_delegatesToPort() {
        Project project = new Project(1L, "Site", "My website", "https://example.com", List.of("c3RyaW5n"), List.of("React"));
        when(projectPersistencePort.findAll()).thenReturn(List.of(project));

        List<Project> result = useCase.getAllProjects();

        assertEquals(1, result.size());
        assertEquals(project, result.get(0));
        verify(projectPersistencePort).findAll();
    }

    @Test
    void getProject_notFound_throws() {
        when(projectPersistencePort.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> useCase.getProject(2L));
    }

    @Test
    void updateProject_found_updatesAndSaves() {
        Project existing = new Project(3L, "Old", "Old description", "https://old.example.com", List.of("a"), List.of("Java"));
        Project updated = new Project(null, "New", "New description", "https://new.example.com", List.of("b"), List.of("Quarkus"));

        when(projectPersistencePort.findById(3L)).thenReturn(Optional.of(existing));
        when(projectPersistencePort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Project result = useCase.updateProject(3L, updated);

        assertEquals(3L, result.getId());
        assertEquals("New", result.getName());
        assertEquals("New description", result.getDescription());
        verify(projectPersistencePort).save(existing);
    }
}