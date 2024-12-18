package metier.entities;

import java.io.Serializable;
import java.sql.Timestamp;

public class Livre implements Serializable {
    private Integer id;
    private String titre;
    private String auteur;
    private String description;
    private String categorie;
    private String imageUrl;
    private boolean disponible;
    private Timestamp dateAjout;
    
    public Livre() {
        super();
    }
    
    public Livre(String titre, String auteur, String description, String categorie, String imageUrl) {
        this.titre = titre;
        this.auteur = auteur;
        this.description = description;
        this.categorie = categorie;
        this.imageUrl = imageUrl;
        this.disponible = true;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    
    public Timestamp getDateAjout() { return dateAjout; }
    public void setDateAjout(Timestamp dateAjout) { this.dateAjout = dateAjout; }
}
