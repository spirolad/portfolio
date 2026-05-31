package fr.spirolad.unit;

import fr.spirolad.application.port.outbound.ExperiencePersistencePort;
import fr.spirolad.application.usecase.ExperienceUseCaseImpl;
import fr.spirolad.domain.exception.ExperienceInvalideException;
import fr.spirolad.domain.exception.ExperienceNotFoundException;
import fr.spirolad.domain.model.Experience;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExperienceUseCaseTest {

    private ExperiencePersistencePort experiencePersistencePort;
    private ExperienceUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        experiencePersistencePort = mock(ExperiencePersistencePort.class);
        useCase = new ExperienceUseCaseImpl(experiencePersistencePort);
    }

    @Test
    void saveExperience_delegatesToPort() {
        Experience in = new Experience(null, "Co", "Pos", List.of("a"), LocalDate.now(), null);
        Experience saved = new Experience(5L, "Co", "Pos", List.of("a"), LocalDate.now(), null);
        when(experiencePersistencePort.save(in)).thenReturn(saved);

        Experience result = useCase.saveExperience(in);

        assertEquals(saved, result);
        verify(experiencePersistencePort).save(in);
    }

    @Test
    void getExperienceById_notFound_throws() {
        when(experiencePersistencePort.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ExperienceNotFoundException.class, () -> useCase.getExperienceById(2L));
    }

    @Test
    void saveExperience_invalidDates_throwsInvalidRequest() {
        Experience valid = new Experience(null, "Co", "Pos", List.of(), LocalDate.of(2022,1,1), LocalDate.of(2022,1,1));
        assertThrows(ExperienceInvalideException.class, () -> valid.setEndDate(LocalDate.of(2020,1,1)));
    }

}
