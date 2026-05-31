package fr.spirolad.domain.model;

import fr.spirolad.domain.exception.EducationInvalideException;
import java.time.LocalDate;

public class Education {

    private Long id;
    private String institution;
    private String degree;
    private LocalDate startDate;
    private LocalDate endDate;

    public Education(Long id, String institution, String degree, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        setInstitution(institution);
        setDegree(degree);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public Long getId() {
        return id;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        if (institution == null || institution.trim().isEmpty()) {
            throw new EducationInvalideException("Institution is required");
        }
        this.institution = institution;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        if (degree == null || degree.trim().isEmpty()) {
            throw new EducationInvalideException("Degree is required");
        }
        this.degree = degree;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new EducationInvalideException("Start date is required");
        }
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate != null && this.startDate != null && endDate.isBefore(this.startDate)) {
            throw new EducationInvalideException("End date must be after or equal to start date");
        }
        this.endDate = endDate;
    }

}
