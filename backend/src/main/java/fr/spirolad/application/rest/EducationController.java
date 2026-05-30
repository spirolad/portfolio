package fr.spirolad.application.rest;

import fr.spirolad.api.EducationsApi;
import fr.spirolad.application.mapper.EducationMapper;
import fr.spirolad.domain.model.Education;
import fr.spirolad.application.usecase.EducationUseCase;
import fr.spirolad.dto.EducationRequest;
import fr.spirolad.dto.EducationResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class EducationController implements EducationsApi {

	private final EducationUseCase educationUseCase;
	private final EducationMapper educationMapper;

	public EducationController(EducationUseCase educationUseCase, EducationMapper educationMapper) {
		this.educationUseCase = educationUseCase;
		this.educationMapper = educationMapper;
	}

	@Override
	public Response createEducation(EducationRequest educationRequest) {
		Education domain = educationMapper.toDomain(educationRequest);
		Education created = educationUseCase.saveEducation(domain);
		EducationResponse dto = educationMapper.toDto(created);
		return Response.status(Response.Status.CREATED).entity(dto).build();
	}

	@Override
	public Response deleteEducation(Long id) {
		educationUseCase.deleteEducationById(id);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@Override
	public Response getEducationById(Long id) {
		Education domain = educationUseCase.getEducationById(id);
		EducationResponse dto = educationMapper.toDto(domain);
		return Response.ok(dto).build();
	}

	@Override
	public Response getEducations() {
		List<Education> educations = educationUseCase.getAllEducations();
		List<EducationResponse> educationResponses = educations.stream().map(educationMapper::toDto).toList();
		return Response.ok(educationResponses).build();
	}

	@Override
	public Response updateEducation(Long id, EducationRequest educationRequest) {
		Education domainFromRequest = educationMapper.toDomain(educationRequest);
		Education updated = educationUseCase.updateEducation(id, domainFromRequest);
		EducationResponse dto = educationMapper.toDto(updated);
		return Response.ok(dto).build();
	}
}
