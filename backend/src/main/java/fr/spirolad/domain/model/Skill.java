package fr.spirolad.domain.model;

public class Skill {
    private final Long id;
    private String name;
    private Category category;

    public Skill(Long id, String name, Category category) {
        this.id = id;
        setName(name);
        setCategory(category);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Skill name is required");
        }
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Skill category is required");
        }
        this.category = category;
    }
}
