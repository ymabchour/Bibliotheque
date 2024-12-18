package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import metier.entities.Livre;

public class LivreDaoImpl implements ILivreDao {
    
    @Override
    public Livre save(Livre l) {
        Connection conn = SingletonConnection.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO livres(titre, auteur, description, categorie, image_url, disponible, date_ajout) VALUES(?,?,?,?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, l.getTitre());
            ps.setString(2, l.getAuteur());
            ps.setString(3, l.getDescription());
            ps.setString(4, l.getCategorie());
            ps.setString(5, l.getImageUrl());
            ps.setBoolean(6, l.isDisponible());
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                l.setId(rs.getInt(1));
            }
            ps.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    @Override
    public List<Livre> livresParMotCle(String mc) {
        List<Livre> livres = new ArrayList<>();
        Connection conn = SingletonConnection.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM livres WHERE titre LIKE ? OR auteur LIKE ? OR description LIKE ? OR categorie LIKE ?");
            ps.setString(1, "%" + mc + "%");
            ps.setString(2, "%" + mc + "%");
            ps.setString(3, "%" + mc + "%");
            ps.setString(4, "%" + mc + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Livre l = new Livre();
                l.setId(rs.getInt("id"));
                l.setTitre(rs.getString("titre"));
                l.setAuteur(rs.getString("auteur"));
                l.setDescription(rs.getString("description"));
                l.setCategorie(rs.getString("categorie"));
                l.setImageUrl(rs.getString("image_url"));
                l.setDisponible(rs.getBoolean("disponible"));
                l.setDateAjout(rs.getTimestamp("date_ajout"));
                livres.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }

    @Override
    public Livre getLivre(Integer id) {
        Connection conn = SingletonConnection.getConnection();
        Livre l = null;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM livres WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                l = new Livre();
                l.setId(rs.getInt("id"));
                l.setTitre(rs.getString("titre"));
                l.setAuteur(rs.getString("auteur"));
                l.setDescription(rs.getString("description"));
                l.setCategorie(rs.getString("categorie"));
                l.setImageUrl(rs.getString("image_url"));
                l.setDisponible(rs.getBoolean("disponible"));
                l.setDateAjout(rs.getTimestamp("date_ajout"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    @Override
    public void updateLivre(Livre l) {
        Connection conn = SingletonConnection.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE livres SET titre=?, auteur=?, description=?, categorie=?, image_url=?, disponible=? WHERE id=?");
            ps.setString(1, l.getTitre());
            ps.setString(2, l.getAuteur());
            ps.setString(3, l.getDescription());
            ps.setString(4, l.getCategorie());
            ps.setString(5, l.getImageUrl());
            ps.setBoolean(6, l.isDisponible());
            ps.setInt(7, l.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteLivre(Integer id) {
        Connection conn = SingletonConnection.getConnection();
        try {
            // D'abord, supprimer les emprunts associés
            PreparedStatement psEmprunts = conn.prepareStatement("DELETE FROM emprunts WHERE livre_id = ?");
            psEmprunts.setInt(1, id);
            psEmprunts.executeUpdate();
            psEmprunts.close();

            // Ensuite, supprimer le livre
            PreparedStatement psLivre = conn.prepareStatement("DELETE FROM livres WHERE id = ?");
            psLivre.setInt(1, id);
            psLivre.executeUpdate();
            psLivre.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression du livre : " + e.getMessage());
        }
    }

    @Override
    public List<Livre> getAllLivres() {
        List<Livre> livres = new ArrayList<>();
        Connection conn = SingletonConnection.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM livres ORDER BY date_ajout DESC");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Livre l = new Livre();
                l.setId(rs.getInt("id"));
                l.setTitre(rs.getString("titre"));
                l.setAuteur(rs.getString("auteur"));
                l.setDescription(rs.getString("description"));
                l.setCategorie(rs.getString("categorie"));
                l.setImageUrl(rs.getString("image_url"));
                l.setDisponible(rs.getBoolean("disponible"));
                l.setDateAjout(rs.getTimestamp("date_ajout"));
                livres.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }

    @Override
    public List<Livre> getLivresDisponibles() {
        List<Livre> livres = new ArrayList<>();
        Connection conn = SingletonConnection.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM livres WHERE disponible = true ORDER BY titre");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Livre l = new Livre();
                l.setId(rs.getInt("id"));
                l.setTitre(rs.getString("titre"));
                l.setAuteur(rs.getString("auteur"));
                l.setDescription(rs.getString("description"));
                l.setCategorie(rs.getString("categorie"));
                l.setImageUrl(rs.getString("image_url"));
                l.setDisponible(rs.getBoolean("disponible"));
                l.setDateAjout(rs.getTimestamp("date_ajout"));
                livres.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }
}
