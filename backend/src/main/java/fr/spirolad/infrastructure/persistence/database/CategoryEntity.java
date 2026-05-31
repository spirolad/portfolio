package fr.spirolad.infrastructure.persistence.database;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class CategoryEntity extends PanacheEntity {
    public String name;
    public String description;

    public CategoryEntity() {
    }

    public CategoryEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
