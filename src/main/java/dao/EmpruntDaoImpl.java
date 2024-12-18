package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import metier.entities.Emprunt;

public class EmpruntDaoImpl implements IEmpruntDao {
    
    @Override
    public Emprunt save(Emprunt e) {
        Connection conn = SingletonConnection.getConnection();
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        
        try {
            // Désactiver l'auto-commit pour la transaction
            conn.setAutoCommit(false);
            
            // Vérifier si le livre est disponible
            ps1 = conn.prepareStatement("SELECT disponible, titre, auteur FROM livres WHERE id = ?");
            ps1.setInt(1, e.getLivreId());
            ResultSet rs = ps1.executeQuery();
            
            if (rs.next() && rs.getBoolean("disponible")) {
                System.out.println("Livre disponible, création de l'emprunt...");
                
                // Stocker les informations du livre
                e.setTitreLivre(rs.getString("titre"));
                e.setAuteurLivre(rs.getString("auteur"));
                
                // Enregistrer l'emprunt
                ps2 = conn.prepareStatement(
                    "INSERT INTO emprunts(livre_id, cin, nom_lecteur, prenom_lecteur, date_retour) VALUES(?,?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
                ps2.setInt(1, e.getLivreId());
                ps2.setString(2, e.getCin());
                ps2.setString(3, e.getNomLecteur());
                ps2.setString(4, e.getPrenomLecteur());
                ps2.setDate(5, e.getDateRetour());
                ps2.executeUpdate();
                
                // Mettre à jour la disponibilité du livre
                ps3 = conn.prepareStatement("UPDATE livres SET disponible = false WHERE id = ?");
                ps3.setInt(1, e.getLivreId());
                ps3.executeUpdate();
                
                ResultSet rs2 = ps2.getGeneratedKeys();
                if (rs2.next()) {
                    e.setId(rs2.getInt(1));
                }
                
                // Valider la transaction
                conn.commit();
                System.out.println("Emprunt créé avec succès, ID: " + e.getId());
                return e;
            } else {
                System.out.println("Livre non disponible");
                conn.rollback();
                return null;
            }
            
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            ex.printStackTrace();
            return null;
        } finally {
            try {
                conn.setAutoCommit(true);
                if (ps1 != null) ps1.close();
                if (ps2 != null) ps2.close();
                if (ps3 != null) ps3.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public List<Emprunt> getEmpruntsEnCours() {
        List<Emprunt> emprunts = new ArrayList<>();
        Connection conn = SingletonConnection.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT e.*, l.titre as titre_livre, l.auteur as auteur_livre " +
                "FROM emprunts e " +
                "JOIN livres l ON e.livre_id = l.id " +
                "WHERE e.retourne = false " +
                "ORDER BY e.date_emprunt DESC");
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                emprunts.add(mapEmprunt(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emprunts;
    }

    @Override
    public List<Emprunt> getHistoriqueEmprunts() {
        List<Emprunt> emprunts = new ArrayList<>();
        Connection conn = SingletonConnection.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT e.*, l.titre as titre_livre, l.auteur as auteur_livre " +
                "FROM emprunts e " +
                "JOIN livres l ON e.livre_id = l.id " +
                "WHERE e.retourne = true " +
                "ORDER BY e.date_emprunt DESC");
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                emprunts.add(mapEmprunt(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emprunts;
    }

    @Override
    public Emprunt getEmprunt(Integer id) {
        Connection conn = SingletonConnection.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT e.*, l.titre as titre_livre, l.auteur as auteur_livre " +
                "FROM emprunts e " +
                "JOIN livres l ON e.livre_id = l.id " +
                "WHERE e.id = ?");
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapEmprunt(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void retournerLivre(Integer id) {
        Connection conn = SingletonConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            
            // Récupérer l'ID du livre
            PreparedStatement ps1 = conn.prepareStatement(
                "SELECT livre_id FROM emprunts WHERE id = ? AND retourne = false");
            ps1.setInt(1, id);
            ResultSet rs = ps1.executeQuery();
            
            if (rs.next()) {
                int livreId = rs.getInt("livre_id");
                
                // Marquer l'emprunt comme retourné
                PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE emprunts SET retourne = true, date_retour = CURRENT_DATE WHERE id = ?");
                ps2.setInt(1, id);
                ps2.executeUpdate();
                
                // Rendre le livre disponible
                PreparedStatement ps3 = conn.prepareStatement(
                    "UPDATE livres SET disponible = true WHERE id = ?");
                ps3.setInt(1, livreId);
                ps3.executeUpdate();
                
                conn.commit();
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Emprunt> getEmpruntsByLecteur(String cin) {
        List<Emprunt> emprunts = new ArrayList<>();
        Connection conn = SingletonConnection.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT e.*, l.titre as titre_livre, l.auteur as auteur_livre " +
                "FROM emprunts e " +
                "JOIN livres l ON e.livre_id = l.id " +
                "WHERE e.cin = ? " +
                "ORDER BY e.date_emprunt DESC");
            ps.setString(1, cin);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                emprunts.add(mapEmprunt(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emprunts;
    }

    @Override
    public int getNombreEmpruntsEnCours(String cin) {
        Connection conn = SingletonConnection.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) as nombre FROM emprunts WHERE cin = ? AND retourne = false");
            ps.setString(1, cin);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("nombre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Emprunt mapEmprunt(ResultSet rs) throws SQLException {
        Emprunt e = new Emprunt();
        e.setId(rs.getInt("id"));
        e.setLivreId(rs.getInt("livre_id"));
        e.setCin(rs.getString("cin"));
        e.setNomLecteur(rs.getString("nom_lecteur"));
        e.setPrenomLecteur(rs.getString("prenom_lecteur"));
        e.setDateEmprunt(rs.getTimestamp("date_emprunt"));
        e.setDateRetour(rs.getDate("date_retour"));
        e.setRetourne(rs.getBoolean("retourne"));
        e.setTitreLivre(rs.getString("titre_livre"));
        e.setAuteurLivre(rs.getString("auteur_livre"));
        return e;
    }

    public boolean retournerEmprunt(Integer id) {
        Connection conn = SingletonConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            
            // Récupérer l'ID du livre
            PreparedStatement ps1 = conn.prepareStatement(
                "SELECT livre_id FROM emprunts WHERE id = ? AND retourne = false");
            ps1.setInt(1, id);
            ResultSet rs = ps1.executeQuery();
            
            if (rs.next()) {
                int livreId = rs.getInt("livre_id");
                
                // Marquer l'emprunt comme retourné
                PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE emprunts SET retourne = true, date_retour = CURRENT_DATE WHERE id = ?");
                ps2.setInt(1, id);
                ps2.executeUpdate();
                
                // Rendre le livre disponible
                PreparedStatement ps3 = conn.prepareStatement(
                    "UPDATE livres SET disponible = true WHERE id = ?");
                ps3.setInt(1, livreId);
                ps3.executeUpdate();
                
                conn.commit();
                return true;
            }
            
            conn.rollback();
            return false;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
