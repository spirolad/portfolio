package fr.spirolad.domain.model;

import fr.spirolad.domain.exception.PortfolioInvalideException;

public class PortfolioProfile {

    private String name;
    private String email;
    private String currentPosition;
    private String photo;

    public PortfolioProfile(String name, String email, String currentPosition, String photo) {
        setName(name);
        setEmail(email);
        setCurrentPosition(currentPosition);
        setPhoto(photo);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isBlank()) {
            throw new PortfolioInvalideException("Le nom du profil de portfolio ne peut pas être vide.");
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isBlank()) {
            throw new PortfolioInvalideException("L'email du profil de portfolio ne peut pas être vide.");
        }
        this.email = email;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        if (currentPosition == null || currentPosition.trim().isBlank()) {
            throw new PortfolioInvalideException("Le poste actuel du profil de portfolio ne peut pas être vide.");
        }
        this.currentPosition = currentPosition;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        if (photo == null || photo.trim().isBlank()) {
            throw new PortfolioInvalideException("La photo du profil de portfolio ne peut pas être vide.");
        }
        this.photo = photo;
    }
}