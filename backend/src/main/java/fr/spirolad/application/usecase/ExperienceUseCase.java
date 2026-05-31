package fr.spirolad.application.usecase;

import fr.spirolad.domain.exception.ResourceNotFoundException;
import fr.spirolad.domain.exception.InvalidRequestException;
import fr.spirolad.domain.model.Experience;
import fr.spirolad.domain.port.ExperiencePersistencePort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ExperienceUseCase {

    private final ExperiencePersistencePort experiencePersistencePort;

    @Inject
    public ExperienceUseCase(ExperiencePersistencePort experiencePersistencePort) {
        this.experiencePersistencePort = experiencePersistencePort;
    }

    public List<Experience> getAllExperiences() {
        return experiencePersistencePort.findAll();
    }

    @Transactional
    public Experience saveExperience(Experience experience) {
        validateExperience(experience);
        return experiencePersistencePort.save(experience);
    }

    @Transactional
    public void deleteExperienceById(Long id) {
        if (experiencePersistencePort.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Experience not found with id: " + id);
        }
        experiencePersistencePort.deleteById(id);
    }

    public Experience getExperienceById(Long id) {
        return experiencePersistencePort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + id));
    }

    @Transactional
    public Experience updateExperience(Long id, Experience updatedExperience) {
        return experiencePersistencePort.findById(id)
                .map(existing -> {
                    validateExperience(updatedExperience);
                    existing.setCompany(updatedExperience.getCompany());
                    existing.setPosition(updatedExperience.getPosition());
                    existing.setMission(updatedExperience.getMission());
                    existing.setStartDate(updatedExperience.getStartDate());
                    existing.setEndDate(updatedExperience.getEndDate());
                    return experiencePersistencePort.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + id));
    }

    private void validateExperience(Experience experience) {
        if (experience == null) {
            throw new InvalidRequestException("Experience must not be null");
        }
        if (experience.getCompany() == null || experience.getCompany().trim().isEmpty()) {
            throw new InvalidRequestException("Company is required");
        }
        if (experience.getPosition() == null || experience.getPosition().trim().isEmpty()) {
            throw new InvalidRequestException("Position is required");
        }
        if (experience.getStartDate() == null) {
            throw new InvalidRequestException("Start date is required");
        }
        if (experience.getEndDate() != null && experience.getEndDate().isBefore(experience.getStartDate())) {
            throw new InvalidRequestException("End date must be after or equal to start date");
        }
    }
}
