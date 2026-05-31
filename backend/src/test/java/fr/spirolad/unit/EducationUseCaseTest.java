package fr.spirolad.unit;

import fr.spirolad.application.usecase.EducationUseCase;
import fr.spirolad.domain.exception.ResourceNotFoundException;
import fr.spirolad.domain.model.Education;
import fr.spirolad.domain.port.EducationPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import fr.spirolad.domain.exception.InvalidRequestException;

public class EducationUseCaseTest {

    private EducationPersistencePort persistencePort;
    private EducationUseCase useCase;

    @BeforeEach
    void setUp() {
        persistencePort = mock(EducationPersistencePort.class);
        useCase = new EducationUseCase(persistencePort);
    }

    @Test
    void getAllEducations_delegatesToPort() {
        Education e = new Education(1L, "Inst", "Deg", LocalDate.now().minusYears(2), LocalDate.now());
        when(persistencePort.findAll()).thenReturn(List.of(e));

        List<Education> result = useCase.getAllEducations();

        assertEquals(1, result.size());
        assertEquals(e, result.get(0));
        verify(persistencePort).findAll();
    }

    @Test
    void saveEducation_delegatesToPort() {
        Education in = new Education(null, "Inst", "Deg", LocalDate.now(), null);
        Education saved = new Education(5L, "Inst", "Deg", LocalDate.now(), null);
        when(persistencePort.save(in)).thenReturn(saved);

        Education result = useCase.saveEducation(in);

        assertEquals(saved, result);
        verify(persistencePort).save(in);
    }

    @Test
    void deleteEducationById_notFound_throws() {
        when(persistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.deleteEducationById(1L));
        verify(persistencePort, never()).deleteById(any());
    }

    @Test
    void deleteEducationById_found_callsDelete() {
        Education existing = new Education(1L, "I", "D", LocalDate.now(), null);
        when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));

        useCase.deleteEducationById(1L);

        verify(persistencePort).deleteById(1L);
    }

    @Test
    void getEducationById_notFound_throws() {
        when(persistencePort.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> useCase.getEducationById(2L));
    }

    @Test
    void getEducationById_found_returnsEducation() {
        Education existing = new Education(2L, "I2", "D2", LocalDate.now(), null);
        when(persistencePort.findById(2L)).thenReturn(Optional.of(existing));

        Education result = useCase.getEducationById(2L);

        assertEquals(existing, result);
    }

    @Test
    void updateEducation_found_updatesAndSaves() {
        Education existing = new Education(3L, "OldInst", "OldDeg", LocalDate.now().minusYears(3), LocalDate.now().minusYears(2));
        Education updated = new Education(null, "NewInst", "NewDeg", LocalDate.now().minusYears(1), LocalDate.now());

        when(persistencePort.findById(3L)).thenReturn(Optional.of(existing));
        when(persistencePort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Education result = useCase.updateEducation(3L, updated);

        assertEquals(3L, result.getId());
        assertEquals("NewInst", result.getInstitution());
        assertEquals("NewDeg", result.getDegree());
        verify(persistencePort).save(existing);
    }

    @Test
    void updateEducation_notFound_throws() {
        when(persistencePort.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> useCase.updateEducation(99L, new Education()));
    }

    @Test
    void saveEducation_invalidDates_throwsInvalidRequest() {
        Education invalid = new Education(null, "Inst", "Deg", LocalDate.of(2022,1,1), LocalDate.of(2020,1,1));
        assertThrows(InvalidRequestException.class, () -> useCase.saveEducation(invalid));
        verify(persistencePort, never()).save(any());
    }

    @Test
    void updateEducation_invalidDates_throwsInvalidRequest() {
        Education existing = new Education(10L, "Old", "OldDeg", LocalDate.now().minusYears(3), null);
        Education updated = new Education(null, "NewInst", "NewDeg", LocalDate.of(2022,1,1), LocalDate.of(2020,1,1));

        when(persistencePort.findById(10L)).thenReturn(Optional.of(existing));

        assertThrows(InvalidRequestException.class, () -> useCase.updateEducation(10L, updated));
        verify(persistencePort, never()).save(any());
    }
}
