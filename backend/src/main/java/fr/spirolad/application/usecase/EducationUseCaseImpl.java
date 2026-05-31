package fr.spirolad.application.usecase;

import fr.spirolad.application.port.inbound.EducationUseCase;
import fr.spirolad.application.port.outbound.EducationPersistencePort;
import fr.spirolad.domain.exception.EducationNotFoundException;
import fr.spirolad.domain.model.Education;

import java.util.List;

public class EducationUseCaseImpl implements EducationUseCase {

    private final EducationPersistencePort educationPersistencePort;

    public EducationUseCaseImpl(EducationPersistencePort educationPersistencePort) {
        this.educationPersistencePort = educationPersistencePort;
    }

    @Override
    public List<Education> getAllEducations() {
        return educationPersistencePort.findAll();
    }

    @Override
    public Education saveEducation(Education education) {
        return educationPersistencePort.save(education);
    }

    @Override
    public void deleteEducationById(Long id) {
        ensureEducationExists(id);
        educationPersistencePort.deleteById(id);
    }

    @Override
    public Education getEducationById(Long id) {
        return educationPersistencePort.findById(id)
                .orElseThrow(() -> new EducationNotFoundException("Education not found with id: " + id));
    }

    @Override
    public Education updateEducation(Long id, Education updatedEducation) {
        Education existingEducation = ensureEducationExists(id);
        applyUpdates(existingEducation, updatedEducation);
        return educationPersistencePort.save(existingEducation);
    }

    private Education ensureEducationExists(Long id) {
        return educationPersistencePort.findById(id)
                .orElseThrow(() -> new EducationNotFoundException("Education not found with id: " + id));
    }

    private void applyUpdates(Education target, Education source) {
        target.setInstitution(source.getInstitution());
        target.setDegree(source.getDegree());
        target.setStartDate(source.getStartDate());
        target.setEndDate(source.getEndDate());
    }
}
