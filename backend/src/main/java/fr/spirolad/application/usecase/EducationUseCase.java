package fr.spirolad.application.usecase;

import fr.spirolad.domain.exception.ResourceNotFoundException;
import fr.spirolad.domain.model.Education;
import fr.spirolad.domain.port.EducationPersistencePort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class EducationUseCase {

    private final EducationPersistencePort educationPersistencePort;

    @Inject
    public EducationUseCase(EducationPersistencePort educationPersistencePort) {
        this.educationPersistencePort = educationPersistencePort;
    }

    public List<Education> getAllEducations() {
        return educationPersistencePort.findAll();
    }

    @Transactional
    public Education saveEducation(Education education) {
        return educationPersistencePort.save(education);
    }

    @Transactional
    public void deleteEducationById(Long id) {
        if (educationPersistencePort.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Education not found with id: " + id);
        }
        educationPersistencePort.deleteById(id);
    }

    public Education getEducationById(Long id) {
        return educationPersistencePort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found with id: " + id));
    }

    @Transactional
    public Education updateEducation(Long id, Education updatedEducation) {
        return educationPersistencePort.findById(id)
                .map(existingEducation -> {
                    existingEducation.setInstitution(updatedEducation.getInstitution());
                    existingEducation.setDegree(updatedEducation.getDegree());
                    existingEducation.setStartDate(updatedEducation.getStartDate());
                    existingEducation.setEndDate(updatedEducation.getEndDate());
                    return educationPersistencePort.save(existingEducation);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Education not found with id: " + id));
    }

}