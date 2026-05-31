package fr.spirolad.infrastructure.rest;

import fr.spirolad.api.EducationsApi;
import fr.spirolad.application.port.inbound.EducationUseCase;
import fr.spirolad.domain.model.Education;
import fr.spirolad.dto.EducationRequest;
import fr.spirolad.dto.EducationResponse;
import fr.spirolad.infrastructure.rest.mapper.EducationRestMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
@Path("/educations")
public class EducationResource implements EducationsApi {

    private final EducationUseCase educationUseCase;
    private final EducationRestMapper educationRestMapper;

    public EducationResource(EducationUseCase educationUseCase, EducationRestMapper educationRestMapper) {
        this.educationUseCase = educationUseCase;
        this.educationRestMapper = educationRestMapper;
    }

    @Override
    public Response createEducation(EducationRequest educationRequest) {
        Education createdEducation = educationUseCase.saveEducation(educationRestMapper.toDomain(educationRequest));
        EducationResponse response = educationRestMapper.toResponse(createdEducation);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @Override
    public Response deleteEducation(Long id) {
        educationUseCase.deleteEducationById(id);
        return Response.noContent().build();
    }

    @Override
    public Response getEducationById(Long id) {
        Education education = educationUseCase.getEducationById(id);
        return Response.ok(educationRestMapper.toResponse(education)).build();
    }

    @Override
    public Response getEducations() {
        List<EducationResponse> responses = educationUseCase.getAllEducations()
                .stream()
                .map(educationRestMapper::toResponse)
                .toList();
        return Response.ok(responses).build();
    }

    @Override
    public Response updateEducation(Long id, EducationRequest educationRequest) {
        Education updatedEducation = educationUseCase.updateEducation(id, educationRestMapper.toDomain(educationRequest));
        return Response.ok(educationRestMapper.toResponse(updatedEducation)).build();
    }
}