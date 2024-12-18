package dao;

import java.util.List;
import metier.entities.Emprunt;

public interface IEmpruntDao {
    public Emprunt save(Emprunt e);
    public List<Emprunt> getEmpruntsEnCours();
    public List<Emprunt> getHistoriqueEmprunts();
    public Emprunt getEmprunt(Integer id);
    public void retournerLivre(Integer id);
    public List<Emprunt> getEmpruntsByLecteur(String cin);
    public int getNombreEmpruntsEnCours(String cin);
}
