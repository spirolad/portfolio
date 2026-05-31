package fr.spirolad.unit;

import fr.spirolad.application.usecase.ExperienceUseCase;
import fr.spirolad.domain.exception.ResourceNotFoundException;
import fr.spirolad.domain.exception.InvalidRequestException;
import fr.spirolad.domain.model.Experience;
import fr.spirolad.domain.port.ExperiencePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExperienceUseCaseTest {

    private ExperiencePersistencePort persistencePort;
    private ExperienceUseCase useCase;

    @BeforeEach
    void setUp() {
        persistencePort = mock(ExperiencePersistencePort.class);
        useCase = new ExperienceUseCase(persistencePort);
    }

    @Test
    void saveExperience_delegatesToPort() {
        Experience in = new Experience(null, "Co", "Pos", List.of("a"), LocalDate.now(), null);
        Experience saved = new Experience(5L, "Co", "Pos", List.of("a"), LocalDate.now(), null);
        when(persistencePort.save(in)).thenReturn(saved);

        Experience result = useCase.saveExperience(in);

        assertEquals(saved, result);
        verify(persistencePort).save(in);
    }

    @Test
    void getExperienceById_notFound_throws() {
        when(persistencePort.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> useCase.getExperienceById(2L));
    }

    @Test
    void saveExperience_invalidDates_throwsInvalidRequest() {
        Experience invalid = new Experience(null, "Co", "Pos", List.of(), LocalDate.of(2022,1,1), LocalDate.of(2020,1,1));
        assertThrows(InvalidRequestException.class, () -> useCase.saveExperience(invalid));
        verify(persistencePort, never()).save(any());
    }

}
