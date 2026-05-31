package fr.spirolad.domain.model;

import fr.spirolad.domain.exception.ExperienceInvalideException;
import java.time.LocalDate;
import java.util.List;

public class Experience {

    private Long id;
    private String company;
    private String position;
    private List<String> mission;
    private LocalDate startDate;
    private LocalDate endDate;

    public Experience(Long id, String company, String position, List<String> mission, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        setCompany(company);
        setPosition(position);
        setMission(mission);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public Long getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        if (company == null || company.trim().isEmpty()) {
            throw new ExperienceInvalideException("Company is required");
        }
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        if (position == null || position.trim().isEmpty()) {
            throw new ExperienceInvalideException("Position is required");
        }
        this.position = position;
    }

    public List<String> getMission() {
        return mission;
    }

    public void setMission(List<String> mission) {
        this.mission = mission;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new ExperienceInvalideException("Start date is required");
        }
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate != null && this.startDate != null && endDate.isBefore(this.startDate)) {
            throw new ExperienceInvalideException("End date must be after or equal to start date");
        }
        this.endDate = endDate;
    }

}
