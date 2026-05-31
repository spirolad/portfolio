package fr.spirolad.infrastructure.database;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "experience")
public class ExperienceEntity extends PanacheEntity {

    public String company;
    public String position;

    @ElementCollection
    @CollectionTable(name = "experience_mission", joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "mission")
    public List<String> mission;

    @Column(name = "start_date")
    public LocalDate startDate;

    @Column(name = "end_date")
    public LocalDate endDate;

}
