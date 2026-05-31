package fr.spirolad.infrastructure.rest;

import fr.spirolad.api.ExperiencesApi;
import fr.spirolad.application.port.inbound.ExperienceUseCase;
import fr.spirolad.domain.model.Experience;
import fr.spirolad.dto.ExperienceRequest;
import fr.spirolad.dto.ExperienceResponse;
import fr.spirolad.infrastructure.rest.mapper.ExperienceRestMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
@Path("/experiences")
public class ExperienceResource implements ExperiencesApi {

    private final ExperienceUseCase experienceUseCase;
    private final ExperienceRestMapper experienceRestMapper;

    public ExperienceResource(ExperienceUseCase experienceUseCase, ExperienceRestMapper experienceRestMapper) {
        this.experienceUseCase = experienceUseCase;
        this.experienceRestMapper = experienceRestMapper;
    }

    @Override
    public Response createExperience(ExperienceRequest experienceRequest) {
        Experience createdExperience = experienceUseCase.saveExperience(experienceRestMapper.toDomain(experienceRequest));
        ExperienceResponse response = experienceRestMapper.toResponse(createdExperience);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @Override
    public Response deleteExperience(Long id) {
        experienceUseCase.deleteExperienceById(id);
        return Response.noContent().build();
    }

    @Override
    public Response getExperienceById(Long id) {
        Experience experience = experienceUseCase.getExperienceById(id);
        return Response.ok(experienceRestMapper.toResponse(experience)).build();
    }

    @Override
    public Response getExperiences() {
        List<ExperienceResponse> responses = experienceUseCase.getAllExperiences()
                .stream()
                .map(experienceRestMapper::toResponse)
                .toList();
        return Response.ok(responses).build();
    }

    @Override
    public Response updateExperience(Long id, ExperienceRequest experienceRequest) {
        Experience updatedExperience = experienceUseCase.updateExperience(id, experienceRestMapper.toDomain(experienceRequest));
        return Response.ok(experienceRestMapper.toResponse(updatedExperience)).build();
    }
}