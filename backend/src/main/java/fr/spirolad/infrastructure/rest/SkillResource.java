package fr.spirolad.infrastructure.rest;

import fr.spirolad.api.SkillsApi;
import fr.spirolad.application.port.inbound.SkillUseCase;
import fr.spirolad.application.port.inbound.CategoryUseCase;
import fr.spirolad.domain.model.Skill;
import fr.spirolad.domain.model.Category;
import fr.spirolad.dto.SkillRequest;
import fr.spirolad.dto.SkillResponse;
import fr.spirolad.dto.CategoryRequest;
import fr.spirolad.dto.CategoryResponse;
import fr.spirolad.infrastructure.rest.mapper.SkillRestMapper;
import fr.spirolad.infrastructure.rest.mapper.CategoryRestMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
@Path("/skills")
public class SkillResource implements SkillsApi {

    private final SkillUseCase skillUseCase;
    private final CategoryUseCase categoryUseCase;
    private final SkillRestMapper skillRestMapper;
    private final CategoryRestMapper categoryRestMapper;

    public SkillResource(SkillUseCase skillUseCase, CategoryUseCase categoryUseCase,
                        SkillRestMapper skillRestMapper, CategoryRestMapper categoryRestMapper) {
        this.skillUseCase = skillUseCase;
        this.categoryUseCase = categoryUseCase;
        this.skillRestMapper = skillRestMapper;
        this.categoryRestMapper = categoryRestMapper;
    }

    @Override
    public Response createSkill(SkillRequest skillRequest) {
        Skill createdSkill = skillUseCase.saveSkill(skillRestMapper.toDomain(skillRequest));
        SkillResponse response = skillRestMapper.toResponse(createdSkill);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @Override
    public Response deleteSkill(Long id) {
        skillUseCase.deleteSkill(id);
        return Response.noContent().build();
    }

    @Override
    public Response getSkillById(Long id) {
        Skill skill = skillUseCase.getSkill(id);
        return Response.ok(skillRestMapper.toResponse(skill)).build();
    }

    @Override
    public Response getSkills() {
        List<SkillResponse> responses = skillUseCase.getAllSkills()
                .stream()
                .map(skillRestMapper::toResponse)
                .toList();
        return Response.ok(responses).build();
    }

    @Override
    public Response updateSkill(Long id, SkillRequest skillRequest) {
        Skill updatedSkill = skillUseCase.updateSkill(id, skillRestMapper.toDomain(skillRequest));
        return Response.ok(skillRestMapper.toResponse(updatedSkill)).build();
    }

    @Override
    public Response createSkillCategory(CategoryRequest categoryRequest) {
        Category createdCategory = categoryUseCase.saveCategory(categoryRestMapper.toDomain(categoryRequest));
        CategoryResponse response = categoryRestMapper.toResponse(createdCategory);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @Override
    public Response deleteSkillCategory(Long id) {
        categoryUseCase.deleteCategory(id);
        return Response.noContent().build();
    }

    @Override
    public Response getSkillCategoryById(Long id) {
        Category category = categoryUseCase.getCategory(id);
        return Response.ok(categoryRestMapper.toResponse(category)).build();
    }

    @Override
    public Response getSkillCategories() {
        List<CategoryResponse> responses = categoryUseCase.getAllCategories()
                .stream()
                .map(categoryRestMapper::toResponse)
                .toList();
        return Response.ok(responses).build();
    }

    @Override
    public Response updateSkillCategory(Long id, CategoryRequest categoryRequest) {
        Category updatedCategory = categoryUseCase.updateCategory(id, categoryRestMapper.toDomain(categoryRequest));
        return Response.ok(categoryRestMapper.toResponse(updatedCategory)).build();
    }
}
