package metier.entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Emprunt implements Serializable {
    private Integer id;
    private Integer livreId;
    private String cin;
    private String nomLecteur;
    private String prenomLecteur;
    private Timestamp dateEmprunt;
    private Date dateRetour;
    private boolean retourne;
    
    // Pour joindre les informations du livre
    private String titreLivre;
    private String auteurLivre;
    
    public Emprunt() {
        super();
    }
    
    public Emprunt(Integer livreId, String cin, String nomLecteur, String prenomLecteur) {
        this.livreId = livreId;
        this.cin = cin;
        this.nomLecteur = nomLecteur;
        this.prenomLecteur = prenomLecteur;
        this.retourne = false;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getLivreId() { return livreId; }
    public void setLivreId(Integer livreId) { this.livreId = livreId; }
    
    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }
    
    public String getNomLecteur() { return nomLecteur; }
    public void setNomLecteur(String nomLecteur) { this.nomLecteur = nomLecteur; }
    
    public String getPrenomLecteur() { return prenomLecteur; }
    public void setPrenomLecteur(String prenomLecteur) { this.prenomLecteur = prenomLecteur; }
    
    public Timestamp getDateEmprunt() { return dateEmprunt; }
    public void setDateEmprunt(Timestamp dateEmprunt) { this.dateEmprunt = dateEmprunt; }
    
    public Date getDateRetour() { return dateRetour; }
    public void setDateRetour(Date dateRetour) { this.dateRetour = dateRetour; }
    
    public boolean isRetourne() { return retourne; }
    public void setRetourne(boolean retourne) { this.retourne = retourne; }
    
    public String getTitreLivre() { return titreLivre; }
    public void setTitreLivre(String titreLivre) { this.titreLivre = titreLivre; }
    
    public String getAuteurLivre() { return auteurLivre; }
    public void setAuteurLivre(String auteurLivre) { this.auteurLivre = auteurLivre; }
}
