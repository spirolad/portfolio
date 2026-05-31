package fr.spirolad.domain.model;

public class Category {
    private final Long id;
    private String name;
    private String description;

    public Category(Long id, String name, String description) {
        this.id = id;
        setName(name);
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name is required");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
