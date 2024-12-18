package web;

import java.io.File;
import java.io.IOException;
import java.util.List;

import dao.ILivreDao;
import dao.LivreDaoImpl;
import dao.IEmpruntDao;
import dao.EmpruntDaoImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import metier.entities.Livre;
import metier.entities.Emprunt;

@WebServlet(name = "cs", urlPatterns = {
    "/",
    "/index",
    "/chercher",
    "/details",
    "/login",
    "/admin/livres",
    "/admin/save",
    "/admin/edit",
    "/admin/update",
    "/admin/supprimer",
    "/admin/saisie",
    "/admin/emprunts",
    "/admin/emprunter",
    "/admin/retourner"
})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 15
)
public class ControleurServlet extends HttpServlet {
    private ILivreDao metier;
    private static final int LIVRES_PAR_PAGE = 10;

    @Override
    public void init() throws ServletException {
        metier = new LivreDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getServletPath();
        
        // Gestion des ressources statiques
        if (path.startsWith("/images/")) {
            String fileName = path.substring("/images/".length());
            String realPath = getServletContext().getRealPath("/images/" + fileName);
            File file = new File(realPath);
            if (file.exists()) {
                response.setContentType(getServletContext().getMimeType(fileName));
                java.nio.file.Files.copy(file.toPath(), response.getOutputStream());
                return;
            }
        }
        
        // Routes principales
        if (path.equals("/") || path.equals("/index")) {
            List<Livre> livres = metier.getAllLivres();
            request.setAttribute("livres", livres);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }
        
        // Gestion de la connexion
        if (path.equals("/login")) {
            if ("logout".equals(request.getParameter("action"))) {
                HttpSession session = request.getSession();
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/");
                return;
            }
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        // Routes admin
        if (path.startsWith("/admin/")) {
            if (!checkAdmin(request, response)) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            
            String adminPath = path.substring("/admin".length());
            
            if ("/livres".equals(adminPath)) {
                List<Livre> livres = metier.getAllLivres();
                request.setAttribute("livres", livres);
                request.getRequestDispatcher("/admin/livres.jsp").forward(request, response);
                return;
            }
            else if ("/saisie".equals(adminPath)) {
                request.getRequestDispatcher("/admin/saisie.jsp").forward(request, response);
                return;
            }
            else if ("/edit".equals(adminPath)) {
                String idStr = request.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) {
                    try {
                        Integer id = Integer.parseInt(idStr);
                        Livre livre = metier.getLivre(id);
                        if (livre != null) {
                            request.setAttribute("livre", livre);
                            request.getRequestDispatcher("/admin/saisie.jsp").forward(request, response);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        // Gérer l'erreur
                    }
                }
                response.sendRedirect(request.getContextPath() + "/admin/livres");
                return;
            }
            else if ("/supprimer".equals(adminPath)) {
                String idStr = request.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) {
                    try {
                        Integer id = Integer.parseInt(idStr);
                        metier.deleteLivre(id);
                        request.getSession().setAttribute("success", "Livre supprimé avec succès");
                    } catch (Exception e) {
                        request.getSession().setAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
                    }
                }
                response.sendRedirect(request.getContextPath() + "/admin/livres");
                return;
            }
            else if ("/emprunts".equals(adminPath)) {
                // Récupérer tous les emprunts
                IEmpruntDao empruntDao = new EmpruntDaoImpl();
                ILivreDao livreDao = new LivreDaoImpl();
                
                List<Emprunt> empruntsEnCours = empruntDao.getEmpruntsEnCours();
                List<Emprunt> empruntsHistorique = empruntDao.getHistoriqueEmprunts();
                List<Livre> livresDisponibles = livreDao.getLivresDisponibles();
                
                request.setAttribute("empruntsEnCours", empruntsEnCours);
                request.setAttribute("empruntsHistorique", empruntsHistorique);
                request.setAttribute("livresDisponibles", livresDisponibles);
                
                request.getRequestDispatcher("/admin/emprunts.jsp").forward(request, response);
                return;
            }
            else if ("/emprunter".equals(adminPath)) {
                ILivreDao livreDao = new LivreDaoImpl();
                List<Livre> livresDisponibles = livreDao.getLivresDisponibles();
                request.setAttribute("livresDisponibles", livresDisponibles);
                request.getRequestDispatcher("/admin/Emprunter.jsp").forward(request, response);
                return;
            }
            else if ("/retourner".equals(adminPath)) {
                String empruntId = request.getParameter("id");
                if (empruntId != null) {
                    try {
                        IEmpruntDao empruntDao = new EmpruntDaoImpl();
                        empruntDao.retournerLivre(Integer.parseInt(empruntId));
                        request.getSession().setAttribute("success", "Livre retourné avec succès");
                    } catch (Exception e) {
                        request.getSession().setAttribute("error", "Erreur lors du retour: " + e.getMessage());
                    }
                }
                response.sendRedirect(request.getContextPath() + "/admin/emprunts");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/admin/livres");
            return;
        }
        
        // Routes publiques
        else if (path.equals("/chercher")) {
            String motCle = request.getParameter("motCle");
            List<Livre> livres = metier.livresParMotCle(motCle);
            request.setAttribute("livres", livres);
            request.setAttribute("motCle", motCle);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }
        else if (path.equals("/details")) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    Integer id = Integer.parseInt(idStr);
                    Livre livre = metier.getLivre(id);
                    if (livre != null) {
                        request.setAttribute("livre", livre);
                        request.getRequestDispatcher("/details.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    // Gérer l'erreur
                }
            }
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        // Si aucune route ne correspond, rediriger vers la page d'accueil
        response.sendRedirect(request.getContextPath() + "/");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getServletPath();

        if (path.equals("/login")) {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            
            // Vérifiez les identifiants (à adapter selon votre système d'authentification)
            if ("admin@bibliotheque.com".equals(email) && "admin123".equals(password)) {
                HttpSession session = request.getSession();
                session.setAttribute("admin", email);
                response.sendRedirect(request.getContextPath() + "/admin/livres");
            } else {
                request.setAttribute("error", "Email ou mot de passe incorrect");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
            return;
        }
        
        if (path.startsWith("/admin/")) {
            if (!checkAdmin(request, response)) return;
            
            String adminPath = path.substring("/admin".length());
            
            if ("/save".equals(adminPath) || "/update".equals(adminPath)) {
                Livre livre = new Livre();
                if ("/update".equals(adminPath)) {
                    String idStr = request.getParameter("id");
                    if (idStr != null && !idStr.isEmpty()) {
                        int id = Integer.parseInt(idStr);
                        livre.setId(id);
                        // Récupérer l'ancienne URL de l'image
                        Livre ancienLivre = metier.getLivre(id);
                        if (ancienLivre != null) {
                            livre.setImageUrl(ancienLivre.getImageUrl());
                        }
                    }
                }
                
                livre.setTitre(request.getParameter("titre"));
                livre.setAuteur(request.getParameter("auteur"));
                livre.setDescription(request.getParameter("description"));
                livre.setCategorie(request.getParameter("categorie"));
                livre.setDisponible(request.getParameter("disponible") != null);
                
                // Gestion de l'image
                Part imagePart = request.getPart("image");
                if (imagePart != null && imagePart.getSize() > 0) {
                    String fileName = imagePart.getSubmittedFileName();
                    String uploadPath = getServletContext().getRealPath("/images/livres/");
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdir();
                    
                    String filePath = uploadPath + File.separator + fileName;
                    imagePart.write(filePath);
                    livre.setImageUrl("images/livres/" + fileName);
                }
                
                if ("/save".equals(adminPath)) {
                    metier.save(livre);
                } else {
                    metier.updateLivre(livre);
                }
                
                response.sendRedirect(request.getContextPath() + "/admin/livres");
            }
            else if ("/emprunter".equals(adminPath)) {
                String livreId = request.getParameter("livreId");
                String cin = request.getParameter("cin");
                String nom = request.getParameter("nom");
                String prenom = request.getParameter("prenom");
                String dateRetourStr = request.getParameter("dateRetour");
                
                if (livreId != null && cin != null && nom != null && prenom != null && dateRetourStr != null) {
                    try {
                        IEmpruntDao empruntDao = new EmpruntDaoImpl();
                        Emprunt emprunt = new Emprunt();
                        emprunt.setLivreId(Integer.parseInt(livreId));
                        emprunt.setCin(cin);
                        emprunt.setNomLecteur(nom);
                        emprunt.setPrenomLecteur(prenom);
                        emprunt.setDateEmprunt(new java.sql.Timestamp(System.currentTimeMillis()));
                        emprunt.setDateRetour(java.sql.Date.valueOf(dateRetourStr));
                        emprunt.setRetourne(false);
                        empruntDao.save(emprunt);
                        request.getSession().setAttribute("success", "Livre emprunté avec succès");
                    } catch (Exception e) {
                        request.setAttribute("error", "Erreur lors de l'emprunt: " + e.getMessage());
                    }
                } else {
                    request.setAttribute("error", "Tous les champs sont obligatoires");
                }
                response.sendRedirect(request.getContextPath() + "/admin/emprunts");
            }
        }
    }

    private boolean checkAdmin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }
}
