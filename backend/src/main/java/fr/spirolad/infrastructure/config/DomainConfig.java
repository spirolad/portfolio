package fr.spirolad.infrastructure.config;

import fr.spirolad.application.port.inbound.EducationUseCase;
import fr.spirolad.application.port.inbound.ExperienceUseCase;
import fr.spirolad.application.port.inbound.SkillUseCase;
import fr.spirolad.application.port.inbound.CategoryUseCase;
import fr.spirolad.application.port.outbound.EducationPersistencePort;
import fr.spirolad.application.port.outbound.ExperiencePersistencePort;
import fr.spirolad.application.port.outbound.SkillPersistencePort;
import fr.spirolad.application.port.outbound.CategoryPersistencePort;
import fr.spirolad.application.usecase.EducationUseCaseImpl;
import fr.spirolad.application.usecase.ExperienceUseCaseImpl;
import fr.spirolad.application.usecase.SkillUseCaseImpl;
import fr.spirolad.application.usecase.CategoryUseCaseImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class DomainConfig {

    @Produces
    public EducationUseCase educationUseCase(EducationPersistencePort educationPersistencePort) {
        return new EducationUseCaseImpl(educationPersistencePort);
    }

    @Produces
    public ExperienceUseCase experienceUseCase(ExperiencePersistencePort experiencePersistencePort) {
        return new ExperienceUseCaseImpl(experiencePersistencePort);
    }

    @Produces
    public SkillUseCase skillUseCase(SkillPersistencePort skillPersistencePort) {
        return new SkillUseCaseImpl(skillPersistencePort);
    }

    @Produces
    public CategoryUseCase categoryUseCase(CategoryPersistencePort categoryPersistencePort) {
        return new CategoryUseCaseImpl(categoryPersistencePort);
    }
}