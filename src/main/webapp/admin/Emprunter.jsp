<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.time.LocalDate" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nouvel Emprunt - Bibliothèque</title>
     <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="../header.jsp" />

    <div class="container" style="margin-top: 80px;">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Nouvel Emprunt</h5>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger" role="alert">
                                ${error}
                            </div>
                        </c:if>
                        
                        <form action="${pageContext.request.contextPath}/admin/emprunter" method="post">
                            <div class="mb-3">
                                <label for="livre" class="form-label">Livre</label>
                                <select class="form-select" id="livre" name="livreId" required>
                                    <option value="">Sélectionner un livre</option>
                                    <c:forEach items="${livresDisponibles}" var="livre">
                                        <option value="${livre.id}">${livre.titre} - ${livre.auteur}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <div class="mb-3">
                                <label for="cin" class="form-label">CIN du lecteur</label>
                                <input type="text" class="form-control" id="cin" name="cin" required
                                       placeholder="Entrez le CIN du lecteur">
                            </div>
                            
                            <div class="mb-3">
                                <label for="nom" class="form-label">Nom du lecteur</label>
                                <input type="text" class="form-control" id="nom" name="nom" required
                                       placeholder="Entrez le nom du lecteur">
                            </div>
                            
                            <div class="mb-3">
                                <label for="prenom" class="form-label">Prénom du lecteur</label>
                                <input type="text" class="form-control" id="prenom" name="prenom" required
                                       placeholder="Entrez le prénom du lecteur">
                            </div>

                            <div class="mb-3">
                                <label for="dateRetour" class="form-label">Date de retour prévue</label>
                                <input type="date" class="form-control" id="dateRetour" name="dateRetour" required
                                       min="${LocalDate.now()}" value="${LocalDate.now().plusDays(14)}">
                            </div>
                            
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="${pageContext.request.contextPath}/admin/emprunts" 
                                   class="btn btn-secondary me-md-2">Annuler</a>
                                <button type="submit" class="btn btn-primary">Enregistrer l'emprunt</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script></body>
</html>
