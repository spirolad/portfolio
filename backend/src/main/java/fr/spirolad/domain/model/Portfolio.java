package fr.spirolad.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {

    private PortfolioProfile generalInfo;
    private List<Education> educations = new ArrayList<>();
    private List<Experience> experiences = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    private List<Skill> skills = new ArrayList<>();

    public Portfolio(PortfolioProfile generalInfo, List<Education> educations, List<Experience> experiences,
                     List<Project> projects, List<Skill> skills) {
        this.generalInfo = generalInfo;
        setEducations(educations);
        setExperiences(experiences);
        setProjects(projects);
        setSkills(skills);
    }

    public PortfolioProfile getGeneralInfo() {
        return generalInfo;
    }

    public void setGeneralInfo(PortfolioProfile generalInfo) {
        this.generalInfo = generalInfo;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations != null ? educations : new ArrayList<>();
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences != null ? experiences : new ArrayList<>();
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects != null ? projects : new ArrayList<>();
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills != null ? skills : new ArrayList<>();
    }
}