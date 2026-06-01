package fr.spirolad.unit;

import fr.spirolad.application.port.outbound.EducationPersistencePort;
import fr.spirolad.application.port.outbound.ExperiencePersistencePort;
import fr.spirolad.application.port.outbound.PortfolioPersistencePort;
import fr.spirolad.application.port.outbound.ProjectPersistencePort;
import fr.spirolad.application.port.outbound.SkillPersistencePort;
import fr.spirolad.application.usecase.PortfolioUseCaseImpl;
import fr.spirolad.domain.model.Category;
import fr.spirolad.domain.model.Education;
import fr.spirolad.domain.model.Experience;
import fr.spirolad.domain.model.Portfolio;
import fr.spirolad.domain.model.PortfolioProfile;
import fr.spirolad.domain.model.Project;
import fr.spirolad.domain.model.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PortfolioUseCaseTest {

    private PortfolioPersistencePort portfolioPersistencePort;
    private EducationPersistencePort educationPersistencePort;
    private ExperiencePersistencePort experiencePersistencePort;
    private ProjectPersistencePort projectPersistencePort;
    private SkillPersistencePort skillPersistencePort;
    private PortfolioUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        portfolioPersistencePort = mock(PortfolioPersistencePort.class);
        educationPersistencePort = mock(EducationPersistencePort.class);
        experiencePersistencePort = mock(ExperiencePersistencePort.class);
        projectPersistencePort = mock(ProjectPersistencePort.class);
        skillPersistencePort = mock(SkillPersistencePort.class);
        useCase = new PortfolioUseCaseImpl(portfolioPersistencePort, educationPersistencePort, experiencePersistencePort, projectPersistencePort, skillPersistencePort);
    }

    @Test
    void getPortfolio_aggregatesAllData() {
        PortfolioProfile profile = new PortfolioProfile(1L, "John", "john@example.com", "Engineer", new byte[]{1, 2});
        Education education = new Education(2L, "Uni", "Degree", LocalDate.now().minusYears(2), null);
        Experience experience = new Experience(3L, "Co", "Role", List.of("mission"), LocalDate.now().minusYears(3), null);
        Project project = new Project(4L, "Site", null, "https://example.com", List.of("a"), List.of("Java"));
        Skill skill = new Skill(5L, "Java", new Category(6L, "Language"));

        when(portfolioPersistencePort.findSingleton()).thenReturn(Optional.of(profile));
        when(educationPersistencePort.findAll()).thenReturn(List.of(education));
        when(experiencePersistencePort.findAll()).thenReturn(List.of(experience));
        when(projectPersistencePort.findAll()).thenReturn(List.of(project));
        when(skillPersistencePort.findAll()).thenReturn(List.of(skill));

        Portfolio result = useCase.getPortfolio();

        assertEquals(profile, result.getGeneralInfo());
        assertEquals(1, result.getEducations().size());
        assertEquals(1, result.getExperiences().size());
        assertEquals(1, result.getProjects().size());
        assertEquals(1, result.getSkills().size());
    }

    @Test
    void updatePortfolio_savesProfileAndRefreshesAggregate() {
        PortfolioProfile profile = new PortfolioProfile(null, "Jane", "jane@example.com", "Designer", new byte[]{9});
        PortfolioProfile persistedProfile = new PortfolioProfile(1L, "Jane", "jane@example.com", "Designer", new byte[]{9});

        when(portfolioPersistencePort.save(profile)).thenReturn(persistedProfile);
        when(portfolioPersistencePort.findSingleton()).thenReturn(Optional.of(persistedProfile));
        when(educationPersistencePort.findAll()).thenReturn(List.of());
        when(experiencePersistencePort.findAll()).thenReturn(List.of());
        when(projectPersistencePort.findAll()).thenReturn(List.of());
        when(skillPersistencePort.findAll()).thenReturn(List.of());

        Portfolio result = useCase.updatePortfolio(profile);

        assertEquals(persistedProfile, result.getGeneralInfo());
        verify(portfolioPersistencePort).save(profile);
    }
}