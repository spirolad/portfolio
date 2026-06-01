package fr.spirolad.domain.model;

public class Category {
    private final Long id;
    private String name;

    public Category(Long id, String name) {
        this.id = id;
        setName(name);
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

}
