package web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ls", urlPatterns = {"/login", "/login/logout"})
public class LoginServlet extends HttpServlet {
    
    private static final String ADMIN_EMAIL = "admin@bibliotheque.com";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String ADMIN_NAME = "Administrateur";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        if (ADMIN_EMAIL.equals(email) && ADMIN_PASSWORD.equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("admin", ADMIN_NAME);
            session.setAttribute("isAdmin", true);
            response.sendRedirect(request.getContextPath() + "/admin/livres");
        } else {
            System.out.println("Login failed for email: " + email);
            request.setAttribute("error", "Email ou mot de passe incorrect");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("logout".equals(action)) {
            HttpSession session = request.getSession();
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }
        
        // Si l'utilisateur est déjà connecté, rediriger vers le dashboard
        HttpSession session = request.getSession();
        if (session.getAttribute("admin") != null) {
            response.sendRedirect(request.getContextPath() + "/admin/livres");
            return;
        }
        
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
