package fr.spirolad.infrastructure.persistence.database;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project")
public class ProjectEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String link;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_screenshot", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "screenshot")
    private List<String> screenshots = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_technology", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "technology")
    private List<String> technologies = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<String> screenshots) {
        this.screenshots = screenshots;
    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }
}