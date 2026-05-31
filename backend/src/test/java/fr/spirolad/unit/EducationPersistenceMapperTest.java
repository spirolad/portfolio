package fr.spirolad.unit;

import fr.spirolad.domain.model.Education;
import fr.spirolad.infrastructure.database.EducationEntity;
import fr.spirolad.infrastructure.mapper.EducationPersistenceMapper;
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
        entity.institution = "Uni";
        entity.degree = "MSc";
        entity.startDate = LocalDate.of(2020, 1, 1);
        entity.endDate = LocalDate.of(2021, 12, 31);

        Education domain = mapper.toDomain(entity);

        assertEquals(entity.id, domain.getId());
        assertEquals(entity.institution, domain.getInstitution());
        assertEquals(entity.degree, domain.getDegree());
        assertEquals(entity.startDate, domain.getStartDate());
        assertEquals(entity.endDate, domain.getEndDate());
    }

    @Test
    void toEntity_mapsAllFields() {
        Education domain = new Education(7L, "School", "BSc", LocalDate.of(2018, 9, 1), LocalDate.of(2020, 6, 30));

        EducationEntity entity = mapper.toEntity(domain);

        assertEquals(domain.getId(), entity.id);
        assertEquals(domain.getInstitution(), entity.institution);
        assertEquals(domain.getDegree(), entity.degree);
        assertEquals(domain.getStartDate(), entity.startDate);
        assertEquals(domain.getEndDate(), entity.endDate);
    }

}
