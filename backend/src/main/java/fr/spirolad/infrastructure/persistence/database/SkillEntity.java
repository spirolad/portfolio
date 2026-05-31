package fr.spirolad.infrastructure.persistence.database;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "skill")
public class SkillEntity extends PanacheEntity {
    public String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    public CategoryEntity category;

    public SkillEntity() {
    }

    public SkillEntity(String name, CategoryEntity category) {
        this.name = name;
        this.category = category;
    }
}
