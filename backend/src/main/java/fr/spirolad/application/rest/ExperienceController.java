package fr.spirolad.application.rest;

import fr.spirolad.api.ExperiencesApi;
import fr.spirolad.application.mapper.ExperienceMapper;
import fr.spirolad.domain.model.Experience;
import fr.spirolad.application.usecase.ExperienceUseCase;
import fr.spirolad.dto.ExperienceRequest;
import fr.spirolad.dto.ExperienceResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
@Path("/experiences")
public class ExperienceController implements ExperiencesApi {

    private final ExperienceUseCase experienceUseCase;
    private final ExperienceMapper experienceMapper;

    public ExperienceController(ExperienceUseCase experienceUseCase, ExperienceMapper experienceMapper) {
        this.experienceUseCase = experienceUseCase;
        this.experienceMapper = experienceMapper;
    }

    @Override
    public Response createExperience(ExperienceRequest experienceRequest) {
        Experience domain = experienceMapper.toDomain(experienceRequest);
        Experience created = experienceUseCase.saveExperience(domain);
        ExperienceResponse dto = experienceMapper.toDto(created);
        return Response.status(Response.Status.CREATED).entity(dto).build();
    }

    @Override
    public Response deleteExperience(Long id) {
        experienceUseCase.deleteExperienceById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response getExperienceById(Long id) {
        Experience domain = experienceUseCase.getExperienceById(id);
        ExperienceResponse dto = experienceMapper.toDto(domain);
        return Response.ok(dto).build();
    }

    @Override
    public Response getExperiences() {
        List<Experience> experiences = experienceUseCase.getAllExperiences();
        List<ExperienceResponse> dtos = experiences.stream().map(experienceMapper::toDto).toList();
        return Response.ok(dtos).build();
    }

    @Override
    public Response updateExperience(Long id, ExperienceRequest experienceRequest) {
        Experience domain = experienceMapper.toDomain(experienceRequest);
        Experience updated = experienceUseCase.updateExperience(id, domain);
        ExperienceResponse dto = experienceMapper.toDto(updated);
        return Response.ok(dto).build();
    }
}
