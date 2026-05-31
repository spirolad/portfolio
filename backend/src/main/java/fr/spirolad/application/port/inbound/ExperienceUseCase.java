package fr.spirolad.application.port.inbound;

import fr.spirolad.domain.model.Experience;

import java.util.List;

public interface ExperienceUseCase {

    List<Experience> getAllExperiences();

    Experience saveExperience(Experience experience);

    void deleteExperienceById(Long id);

    Experience getExperienceById(Long id);

    Experience updateExperience(Long id, Experience updatedExperience);
}