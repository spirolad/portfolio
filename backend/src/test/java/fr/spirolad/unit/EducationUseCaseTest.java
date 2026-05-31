package fr.spirolad.unit;

import fr.spirolad.application.port.outbound.EducationPersistencePort;
import fr.spirolad.application.usecase.EducationUseCaseImpl;
import fr.spirolad.domain.exception.EducationInvalideException;
import fr.spirolad.domain.exception.EducationNotFoundException;
import fr.spirolad.domain.model.Education;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EducationUseCaseTest {

    private EducationPersistencePort educationPersistencePort;
    private EducationUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        educationPersistencePort = mock(EducationPersistencePort.class);
        useCase = new EducationUseCaseImpl(educationPersistencePort);
    }

    @Test
    void getAllEducations_delegatesToPort() {
        Education e = new Education(1L, "Inst", "Deg", LocalDate.now().minusYears(2), LocalDate.now());
        when(educationPersistencePort.findAll()).thenReturn(List.of(e));

        List<Education> result = useCase.getAllEducations();

        assertEquals(1, result.size());
        assertEquals(e, result.get(0));
        verify(educationPersistencePort).findAll();
    }

    @Test
    void saveEducation_delegatesToPort() {
        Education in = new Education(null, "Inst", "Deg", LocalDate.now(), null);
        Education saved = new Education(5L, "Inst", "Deg", LocalDate.now(), null);
        when(educationPersistencePort.save(in)).thenReturn(saved);

        Education result = useCase.saveEducation(in);

        assertEquals(saved, result);
        verify(educationPersistencePort).save(in);
    }

    @Test
    void deleteEducationById_notFound_throws() {
        when(educationPersistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EducationNotFoundException.class, () -> useCase.deleteEducationById(1L));
        verify(educationPersistencePort, never()).deleteById(any());
    }

    @Test
    void deleteEducationById_found_callsDelete() {
        Education existing = new Education(1L, "I", "D", LocalDate.now(), null);
        when(educationPersistencePort.findById(1L)).thenReturn(Optional.of(existing));

        useCase.deleteEducationById(1L);

        verify(educationPersistencePort).deleteById(1L);
    }

    @Test
    void getEducationById_notFound_throws() {
        when(educationPersistencePort.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EducationNotFoundException.class, () -> useCase.getEducationById(2L));
    }

    @Test
    void getEducationById_found_returnsEducation() {
        Education existing = new Education(2L, "I2", "D2", LocalDate.now(), null);
        when(educationPersistencePort.findById(2L)).thenReturn(Optional.of(existing));

        Education result = useCase.getEducationById(2L);

        assertEquals(existing, result);
    }

    @Test
    void updateEducation_found_updatesAndSaves() {
        Education existing = new Education(3L, "OldInst", "OldDeg", LocalDate.now().minusYears(3), LocalDate.now().minusYears(2));
        Education updated = new Education(null, "NewInst", "NewDeg", LocalDate.now().minusYears(1), LocalDate.now());

        when(educationPersistencePort.findById(3L)).thenReturn(Optional.of(existing));
        when(educationPersistencePort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Education result = useCase.updateEducation(3L, updated);

        assertEquals(3L, result.getId());
        assertEquals("NewInst", result.getInstitution());
        assertEquals("NewDeg", result.getDegree());
        verify(educationPersistencePort).save(existing);
    }

    @Test
    void updateEducation_notFound_throws() {
        when(educationPersistencePort.findById(99L)).thenReturn(Optional.empty());
        Education updated = new Education(null, "I", "D", LocalDate.now(), null);
        assertThrows(EducationNotFoundException.class, () -> useCase.updateEducation(99L, updated));
    }

    @Test
    void saveEducation_invalidDates_throwsInvalidRequest() {
        Education valid = new Education(null, "Inst", "Deg", LocalDate.of(2022,1,1), LocalDate.of(2022,1,1));
        assertThrows(EducationInvalideException.class, () -> valid.setEndDate(LocalDate.of(2020,1,1)));
    }

    @Test
    void updateEducation_invalidDates_throwsInvalidRequest() {
        Education existing = new Education(10L, "Old", "OldDeg", LocalDate.now().minusYears(3), null);

        when(educationPersistencePort.findById(10L)).thenReturn(Optional.of(existing));

        assertThrows(EducationInvalideException.class, () -> existing.setEndDate(LocalDate.of(2020,1,1)));
    }
}
