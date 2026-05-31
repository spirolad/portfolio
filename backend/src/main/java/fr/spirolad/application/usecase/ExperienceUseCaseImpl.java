package fr.spirolad.application.usecase;

import fr.spirolad.application.port.inbound.ExperienceUseCase;
import fr.spirolad.application.port.outbound.ExperiencePersistencePort;
import fr.spirolad.domain.exception.ExperienceInvalideException;
import fr.spirolad.domain.exception.ExperienceNotFoundException;
import fr.spirolad.domain.model.Experience;

import java.util.List;

public class ExperienceUseCaseImpl implements ExperienceUseCase {

    private final ExperiencePersistencePort experiencePersistencePort;

    public ExperienceUseCaseImpl(ExperiencePersistencePort experiencePersistencePort) {
        this.experiencePersistencePort = experiencePersistencePort;
    }

    @Override
    public List<Experience> getAllExperiences() {
        return experiencePersistencePort.findAll();
    }

    @Override
    public Experience saveExperience(Experience experience) {
        return experiencePersistencePort.save(experience);
    }

    @Override
    public void deleteExperienceById(Long id) {
        ensureExperienceExists(id);
        experiencePersistencePort.deleteById(id);
    }

    @Override
    public Experience getExperienceById(Long id) {
        return experiencePersistencePort.findById(id)
                .orElseThrow(() -> new ExperienceNotFoundException("Experience not found with id: " + id));
    }

    @Override
    public Experience updateExperience(Long id, Experience updatedExperience) {
        Experience existingExperience = ensureExperienceExists(id);
        applyUpdates(existingExperience, updatedExperience);
        return experiencePersistencePort.save(existingExperience);
    }

    private Experience ensureExperienceExists(Long id) {
        return experiencePersistencePort.findById(id)
                .orElseThrow(() -> new ExperienceNotFoundException("Experience not found with id: " + id));
    }



    private void applyUpdates(Experience target, Experience source) {
        target.setCompany(source.getCompany());
        target.setPosition(source.getPosition());
        target.setMission(source.getMission());
        target.setStartDate(source.getStartDate());
        target.setEndDate(source.getEndDate());
    }
}