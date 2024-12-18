package dao;

import java.util.List;
import metier.entities.Livre;

public interface ILivreDao {
    public Livre save(Livre l);
    public List<Livre> getAllLivres();
    public Livre getLivre(Integer id);
    public List<Livre> livresParMotCle(String mc);
    public void updateLivre(Livre l);
    public void deleteLivre(Integer id);
    public List<Livre> getLivresDisponibles();
}
