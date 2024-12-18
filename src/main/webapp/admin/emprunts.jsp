<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.sql.Date" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des Emprunts - Bibliothèque</title>
<%--     <link href="${pageContext.request.contextPath}/BS/bootstrap.min.css" rel="stylesheet">
 --%>
     <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
 
     <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="../header.jsp" />

    <div class="container mt-4">
        <div class="card">
            <div class="card-header">
                <div class="row">
                    <div class="col">
                        <h3>Liste des Emprunts</h3>
                    </div>
                    <div class="col text-end">
                        <a href="${pageContext.request.contextPath}/admin/emprunter" class="btn btn-primary">
                            <i class="bi bi-plus"></i> Nouvel Emprunt
                        </a>
                    </div>
                </div>
            </div>
            <div class="card-body">
                <ul class="nav nav-tabs mb-3">
                    <li class="nav-item">
                        <a class="nav-link ${empty param.tab || param.tab == 'encours' ? 'active' : ''}" 
                           href="#encours" data-bs-toggle="tab">En cours</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${param.tab == 'historique' ? 'active' : ''}" 
                           href="#historique" data-bs-toggle="tab">Historique</a>
                    </li>
                </ul>

                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-danger">
                        ${sessionScope.error}
                        <% session.removeAttribute("error"); %>
                    </div>
                </c:if>
                <c:if test="${not empty sessionScope.success}">
                    <div class="alert alert-success">
                        ${sessionScope.success}
                        <% session.removeAttribute("success"); %>
                    </div>
                </c:if>

                <div class="tab-content">
                    <div class="tab-pane fade show ${empty param.tab || param.tab == 'encours' ? 'active' : ''}" id="encours">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Livre</th>
                                    <th>Lecteur</th>
                                    <th>Date d'emprunt</th>
                                    <th>Statut</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${empruntsEnCours}" var="e">
                                    <tr>
                                        <td>${e.id}</td>
                                        <td>${e.titreLivre}</td>
                                        <td>${e.nomLecteur} ${e.prenomLecteur}</td>
                                        <td><fmt:formatDate value="${e.dateEmprunt}" pattern="dd/MM/yyyy"/></td>
                                        <td>
                                            <span class="badge bg-warning">En cours</span>
                                        </td>
                                        <td>
                                            <a href="javascript:void(0)" 
                                               onclick="confirmReturn(${e.id})"
                                               class="btn btn-success btn-sm">
                                               <i class="bi bi-check-circle"></i> Retourner
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    
                    <div class="tab-pane fade ${param.tab == 'historique' ? 'show active' : ''}" id="historique">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Livre</th>
                                    <th>Lecteur</th>
                                    <th>Date d'emprunt</th>
                                    <th>Date de retour</th>
                                    <th>Statut</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${empruntsHistorique}" var="e">
                                    <tr>
                                        <td>${e.id}</td>
                                        <td>${e.titreLivre}</td>
                                        <td>${e.nomLecteur} ${e.prenomLecteur}</td>
                                        <td><fmt:formatDate value="${e.dateEmprunt}" pattern="dd/MM/yyyy"/></td>
                                        <td><fmt:formatDate value="${e.dateRetour}" pattern="dd/MM/yyyy"/></td>
                                        <td>
                                            <span class="badge bg-success">Retourné</span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function confirmReturn(id) {
            if (confirm('Êtes-vous sûr de vouloir retourner ce livre ?')) {
                window.location.href = '${pageContext.request.contextPath}/admin/retourner?id=' + id;
            }
        }
    </script>
</body>
</html>
