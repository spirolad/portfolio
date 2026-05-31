package fr.spirolad.application.port.inbound;

import fr.spirolad.domain.model.Education;

import java.util.List;

public interface EducationUseCase {

    List<Education> getAllEducations();

    Education saveEducation(Education education);

    void deleteEducationById(Long id);

    Education getEducationById(Long id);

    Education updateEducation(Long id, Education updatedEducation);
}
