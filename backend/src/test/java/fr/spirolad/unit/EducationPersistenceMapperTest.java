package fr.spirolad.unit;

import fr.spirolad.domain.model.Education;
import fr.spirolad.infrastructure.persistence.database.EducationEntity;
import fr.spirolad.infrastructure.persistence.mapper.EducationPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EducationPersistenceMapperTest {

    private final EducationPersistenceMapper mapper = Mappers.getMapper(EducationPersistenceMapper.class);

    @Test
    void toDomain_mapsAllFields() {
        EducationEntity entity = new EducationEntity();
        entity.id = 42L;
        entity.setInstitution("Uni");
        entity.setDegree("MSc");
        entity.setStartDate(LocalDate.of(2020, 1, 1));
        entity.setEndDate(LocalDate.of(2021, 12, 31));

        Education domain = mapper.toDomain(entity);

        assertEquals(entity.id, domain.getId());
        assertEquals(entity.getInstitution(), domain.getInstitution());
        assertEquals(entity.getDegree(), domain.getDegree());
        assertEquals(entity.getStartDate(), domain.getStartDate());
        assertEquals(entity.getEndDate(), domain.getEndDate());
    }

    @Test
    void toEntity_mapsAllFields() {
        Education domain = new Education(7L, "School", "BSc", LocalDate.of(2018, 9, 1), LocalDate.of(2020, 6, 30));

        EducationEntity entity = mapper.toEntity(domain);

        assertEquals(domain.getId(), entity.id);
        assertEquals(domain.getInstitution(), entity.getInstitution());
        assertEquals(domain.getDegree(), entity.getDegree());
        assertEquals(domain.getStartDate(), entity.getStartDate());
        assertEquals(domain.getEndDate(), entity.getEndDate());
    }

}
