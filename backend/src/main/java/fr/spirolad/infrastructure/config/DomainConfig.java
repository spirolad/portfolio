package fr.spirolad.infrastructure.config;

import fr.spirolad.application.port.inbound.EducationUseCase;
import fr.spirolad.application.port.inbound.ExperienceUseCase;
import fr.spirolad.application.port.outbound.EducationPersistencePort;
import fr.spirolad.application.port.outbound.ExperiencePersistencePort;
import fr.spirolad.application.usecase.EducationUseCaseImpl;
import fr.spirolad.application.usecase.ExperienceUseCaseImpl;
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
}