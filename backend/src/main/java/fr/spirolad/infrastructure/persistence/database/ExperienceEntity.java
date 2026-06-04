package fr.spirolad.infrastructure.persistence.database;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "experience")
public class ExperienceEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String company;

    @NotNull
    private String position;

    @ElementCollection
    @CollectionTable(name = "experience_mission", joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "mission")
    private List<String> mission;

    @Column(name = "start_date")
    @NotNull
    private LocalDate startDate;

    @Column(name = "end_date")
    @NotNull
    private LocalDate endDate;

    public Long getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
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
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
