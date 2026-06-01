package fr.spirolad.infrastructure.rest;

import fr.spirolad.api.ProjectsApi;
import fr.spirolad.application.port.inbound.ProjectUseCase;
import fr.spirolad.domain.model.Project;
import fr.spirolad.dto.ProjectResponse;
import fr.spirolad.dto.ProjectUploadRequest;
import fr.spirolad.infrastructure.rest.mapper.ProjectRestMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
@Path("/projects")
public class ProjectResource implements ProjectsApi {

    private final ProjectUseCase projectUseCase;
    private final ProjectRestMapper projectRestMapper;

    public ProjectResource(ProjectUseCase projectUseCase, ProjectRestMapper projectRestMapper) {
        this.projectUseCase = projectUseCase;
        this.projectRestMapper = projectRestMapper;
    }

    @Override
    public Response createProject(ProjectUploadRequest projectUploadRequest) {
        Project createdProject = projectUseCase.saveProject(projectRestMapper.toDomain(projectUploadRequest));
        ProjectResponse response = projectRestMapper.toResponse(createdProject);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @Override
    public Response deleteProject(Long id) {
        projectUseCase.deleteProject(id);
        return Response.noContent().build();
    }

    @Override
    public Response getProjectById(Long id) {
        Project project = projectUseCase.getProject(id);
        return Response.ok(projectRestMapper.toResponse(project)).build();
    }

    @Override
    public Response getProjects() {
        List<ProjectResponse> responses = projectUseCase.getAllProjects().stream()
                .map(projectRestMapper::toResponse)
                .toList();
        return Response.ok(responses).build();
    }

    @Override
    public Response updateProject(Long id, ProjectUploadRequest projectUploadRequest) {
        Project updatedProject = projectUseCase.updateProject(id, projectRestMapper.toDomain(projectUploadRequest));
        return Response.ok(projectRestMapper.toResponse(updatedProject)).build();
    }
}