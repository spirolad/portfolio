package fr.spirolad.unit;

import fr.spirolad.application.port.outbound.SkillPersistencePort;
import fr.spirolad.application.usecase.SkillUseCaseImpl;
import fr.spirolad.domain.exception.SkillNotFoundException;
import fr.spirolad.domain.model.Category;
import fr.spirolad.domain.model.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SkillUseCaseTest {

    private SkillPersistencePort skillPersistencePort;
    private SkillUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        skillPersistencePort = mock(SkillPersistencePort.class);
        useCase = new SkillUseCaseImpl(skillPersistencePort);
    }

    @Test
    void getAllSkills_delegatesToPort() {
        Skill skill = new Skill(1L, "Java", new Category(10L, "Language"));
        when(skillPersistencePort.findAll()).thenReturn(List.of(skill));

        List<Skill> result = useCase.getAllSkills();

        assertEquals(1, result.size());
        assertEquals(skill, result.get(0));
        verify(skillPersistencePort).findAll();
    }

    @Test
    void saveSkill_delegatesToPort() {
        Skill input = new Skill(null, "Java", new Category(10L, "Language"));
        Skill saved = new Skill(5L, "Java", new Category(10L, "Language"));
        when(skillPersistencePort.save(input)).thenReturn(saved);

        Skill result = useCase.saveSkill(input);

        assertEquals(saved, result);
        verify(skillPersistencePort).save(input);
    }

    @Test
    void getSkill_notFound_throws() {
        when(skillPersistencePort.findById(2L)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class, () -> useCase.getSkill(2L));
    }

    @Test
    void getSkill_found_returnsSkill() {
        Skill skill = new Skill(2L, "Java", new Category(10L, "Language"));
        when(skillPersistencePort.findById(2L)).thenReturn(Optional.of(skill));

        Skill result = useCase.getSkill(2L);

        assertEquals(skill, result);
    }

    @Test
    void updateSkill_found_updatesAndSaves() {
        Skill existing = new Skill(3L, "Old", new Category(10L, "Language"));
        Skill updated = new Skill(null, "New", new Category(11L, "Framework"));

        when(skillPersistencePort.findById(3L)).thenReturn(Optional.of(existing));
        when(skillPersistencePort.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Skill result = useCase.updateSkill(3L, updated);

        assertEquals(3L, result.getId());
        assertEquals("New", result.getName());
        assertEquals(11L, result.getCategory().getId());
        verify(skillPersistencePort).update(existing);
    }

    @Test
    void updateSkill_notFound_throws() {
        when(skillPersistencePort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class, () -> useCase.updateSkill(99L, new Skill(null, "A", new Category(1L, "Cat"))));
    }

    @Test
    void deleteSkill_delegatesToPort() {
        useCase.deleteSkill(12L);

        verify(skillPersistencePort).deleteById(12L);
    }
}