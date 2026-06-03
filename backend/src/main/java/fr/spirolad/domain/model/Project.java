package fr.spirolad.domain.model;

import fr.spirolad.domain.exception.ProjectInvalideException;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private Long id;
    private String name;
    private String description;
    private String link;
    private List<String> screenshots = new ArrayList<>();
    private List<String> technologies = new ArrayList<>();

    public Project(Long id, String name, String description, String link, List<String> screenshots, List<String> technologies) {
        this.id = id;
        setName(name);
        setDescription(description);
        setLink(link);
        setScreenshots(screenshots);
        setTechnologies(technologies);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Project name is required");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isBlank()) {
            throw new ProjectInvalideException("Project description is required");
        }
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        if (link == null || link.isBlank()) {
            throw new ProjectInvalideException("Project link is required");
        }
        this.link = link;
    }

    public List<String> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<String> screenshots) {
        this.screenshots = screenshots != null ? screenshots : new ArrayList<>();
    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies != null ? technologies : new ArrayList<>();
    }
}
