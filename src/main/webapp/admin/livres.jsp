<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des Livres - Bibliothèque</title>
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
                        <h3>Liste des Livres</h3>
                    </div>
                    <div class="col text-end">
                        <a href="${pageContext.request.contextPath}/admin/saisie" class="btn btn-primary">
                            <i class="bi bi-plus"></i> Nouveau Livre
                        </a>
                    </div>
                </div>
            </div>
            <div class="card-body">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        ${error}
                    </div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="alert alert-success">
                        ${success}
                    </div>
                </c:if>

                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Titre</th>
                            <th>Auteur</th>
                            <th>Disponible</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${livres}" var="l">
                            <tr>
                                <td>${l.id}</td>
                                <td>${l.titre}</td>
                                <td>${l.auteur}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${l.disponible}">
                                            <span class="badge bg-success">Disponible</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-warning">Emprunté</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/details?id=${l.id}" 
                                       class="btn btn-info btn-sm">Détails</a>
                                    <a href="${pageContext.request.contextPath}/admin/edit?id=${l.id}" 
                                       class="btn btn-warning btn-sm">
                                       <i class="bi bi-pencil"></i> Modifier
                                    </a>
                                    <a href="javascript:void(0)" 
                                       onclick="confirmDelete(${l.id})"
                                       class="btn btn-danger btn-sm">
                                       <i class="bi bi-trash"></i> Supprimer
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script>
        function confirmDelete(id) {
            if (confirm('Êtes-vous sûr de vouloir supprimer ce livre ?')) {
                window.location.href = '${pageContext.request.contextPath}/admin/supprimer?id=' + id;
            }
        }
    </script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script></body>
</html>
