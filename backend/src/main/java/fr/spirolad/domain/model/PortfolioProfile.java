package fr.spirolad.domain.model;

public class PortfolioProfile {

    private Long id;
    private String name;
    private String email;
    private String currentPosition;
    private byte[] photo;

    public PortfolioProfile(Long id, String name, String email, String currentPosition, byte[] photo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.currentPosition = currentPosition;
        this.photo = photo;
    }

    public Long getId() {
        return id;
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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}