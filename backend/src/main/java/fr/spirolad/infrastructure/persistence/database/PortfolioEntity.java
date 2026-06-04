package fr.spirolad.infrastructure.persistence.database;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "portfolio")
public class PortfolioEntity extends PanacheEntityBase {

    @Id
    private Long id = 1L;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @Column(name = "current_position")
    @NotNull
    private String currentPosition;

    @Column(name = "photo")
    @NotNull
    private String photo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}