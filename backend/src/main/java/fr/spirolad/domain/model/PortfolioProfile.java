package fr.spirolad.domain.model;

public class PortfolioProfile {

    private String name;
    private String email;
    private String currentPosition;
    private String photo;

    public PortfolioProfile(String name, String email, String currentPosition, String photo) {
        this.name = name;
        this.email = email;
        this.currentPosition = currentPosition;
        this.photo = photo;
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