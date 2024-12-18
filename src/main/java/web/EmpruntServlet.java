package web;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import dao.EmpruntDaoImpl;
import dao.IEmpruntDao;
import dao.ILivreDao;
import dao.LivreDaoImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import metier.entities.Emprunt;
import metier.entities.Livre;

@WebServlet(urlPatterns = {"/admin/emprunts", "/admin/emprunter", "/admin/retourner"})
public class EmpruntServlet extends HttpServlet {
    private IEmpruntDao empruntMetier;
    private ILivreDao livreMetier;

    @Override
    public void init() throws ServletException {
        empruntMetier = new EmpruntDaoImpl();
        livreMetier = new LivreDaoImpl();
        System.out.println("EmpruntServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        System.out.println("EmpruntServlet Path: " + path);

        // Vérification de l'authentification admin
        HttpSession session = request.getSession();
        if (session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if ("/admin/emprunts".equals(path)) {
            System.out.println("Loading emprunts page");
            String tab = request.getParameter("tab");
            List<Emprunt> emprunts;
            
            if ("historique".equals(tab)) {
                emprunts = empruntMetier.getHistoriqueEmprunts();
            } else {
                emprunts = empruntMetier.getEmpruntsEnCours();
            }
            
            request.setAttribute("emprunts", emprunts);
            request.setAttribute("livresDisponibles", livreMetier.getLivresDisponibles());
            request.getRequestDispatcher("/admin/emprunts.jsp").forward(request, response);
        }
        else if ("/admin/emprunter".equals(path)) {
            System.out.println("Loading emprunter form");
            // Vérifier si l'utilisateur a déjà 3 emprunts en cours
            String cin = request.getParameter("cin");
            if (cin != null && !cin.isEmpty()) {
                int empruntsEnCours = empruntMetier.getNombreEmpruntsEnCours(cin);
                if (empruntsEnCours >= 3) {
                    request.setAttribute("error", "Le lecteur a déjà atteint la limite de 3 emprunts en cours.");
                }
            }
            
            // Charger la liste des livres disponibles pour le formulaire
            List<Livre> livresDisponibles = livreMetier.getLivresDisponibles();
            System.out.println("Nombre de livres disponibles: " + (livresDisponibles != null ? livresDisponibles.size() : 0));
            request.setAttribute("livresDisponibles", livresDisponibles);
            request.getRequestDispatcher("/admin/Emprunter.jsp").forward(request, response);
        }
        else if ("/admin/retourner".equals(path)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    empruntMetier.retournerLivre(id);
                } catch (NumberFormatException e) {
                    // Gérer l'erreur
                }
            }
            response.sendRedirect(request.getContextPath() + "/admin/emprunts");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Vérification de l'authentification admin
        HttpSession session = request.getSession();
        if (session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String path = request.getServletPath();
        System.out.println("EmpruntServlet POST Path: " + path);

        if ("/admin/emprunter".equals(path)) {
            String livreIdStr = request.getParameter("livreId");
            String cin = request.getParameter("cin");
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String dateRetourStr = request.getParameter("dateRetour");
            
            System.out.println("Tentative d'emprunt - Livre ID: " + livreIdStr + ", CIN: " + cin + ", Date retour: " + dateRetourStr);
            
            if (livreIdStr != null && !livreIdStr.isEmpty() && 
                cin != null && !cin.isEmpty() &&
                nom != null && !nom.isEmpty() &&
                prenom != null && !prenom.isEmpty() &&
                dateRetourStr != null && !dateRetourStr.isEmpty()) {
                
                try {
                    // Vérifier le nombre d'emprunts en cours
                    int empruntsEnCours = empruntMetier.getNombreEmpruntsEnCours(cin);
                    if (empruntsEnCours >= 3) {
                        request.setAttribute("error", "Le lecteur a déjà atteint la limite de 3 emprunts en cours.");
                        request.setAttribute("livresDisponibles", livreMetier.getLivresDisponibles());
                        request.getRequestDispatcher("/admin/Emprunter.jsp").forward(request, response);
                        return;
                    }
                    
                    Integer livreId = Integer.parseInt(livreIdStr);
                    Emprunt emprunt = new Emprunt(livreId, cin, nom, prenom);
                    
                    // Convertir la date de retour
                    LocalDate dateRetour = LocalDate.parse(dateRetourStr);
                    emprunt.setDateRetour(Date.valueOf(dateRetour));
                    
                    Emprunt savedEmprunt = empruntMetier.save(emprunt);
                    if (savedEmprunt == null) {
                        System.err.println("Échec de l'emprunt - Le livre n'est peut-être pas disponible");
                        request.setAttribute("error", "Le livre n'est pas disponible pour l'emprunt.");
                        request.setAttribute("livresDisponibles", livreMetier.getLivresDisponibles());
                        request.getRequestDispatcher("/admin/Emprunter.jsp").forward(request, response);
                        return;
                    } else {
                        System.out.println("Emprunt réussi - ID: " + savedEmprunt.getId());
                        response.sendRedirect(request.getContextPath() + "/admin/emprunts");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Erreur de format pour l'ID du livre: " + livreIdStr);
                    e.printStackTrace();
                    request.setAttribute("error", "Format d'ID de livre invalide.");
                } catch (Exception e) {
                    System.err.println("Erreur lors de l'emprunt: " + e.getMessage());
                    e.printStackTrace();
                    request.setAttribute("error", "Une erreur est survenue lors de l'emprunt.");
                }
                request.setAttribute("livresDisponibles", livreMetier.getLivresDisponibles());
                request.getRequestDispatcher("/admin/Emprunter.jsp").forward(request, response);
                return;
            } else {
                System.err.println("Données d'emprunt incomplètes");
                request.setAttribute("error", "Veuillez remplir tous les champs.");
                request.setAttribute("livresDisponibles", livreMetier.getLivresDisponibles());
                request.getRequestDispatcher("/admin/Emprunter.jsp").forward(request, response);
                return;
            }
        }
        else if ("/admin/retourner".equals(path)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    Integer id = Integer.parseInt(idStr);
                    empruntMetier.retournerLivre(id);
                    System.out.println("Livre retourné avec succès - ID: " + id);
                } catch (NumberFormatException e) {
                    System.err.println("Format d'ID invalide pour le retour: " + idStr);
                    e.printStackTrace();
                    request.setAttribute("error", "Format d'ID invalide pour le retour.");
                } catch (Exception e) {
                    System.err.println("Erreur lors du retour: " + e.getMessage());
                    e.printStackTrace();
                    request.setAttribute("error", "Une erreur est survenue lors du retour.");
                }
            }
            response.sendRedirect(request.getContextPath() + "/admin/emprunts");
        }
    }
}
