<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${livre.titre} - Détails</title>
     <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .book-image {
            max-height: 400px;
            object-fit: contain;
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />


    <div class="container py-5">
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/index">Accueil</a></li>
                <li class="breadcrumb-item active" aria-current="page">Détails du livre</li>
            </ol>
        </nav>

        <div class="row">
            <div class="col-md-4">
                <img src="${livre.imageUrl}" class="img-fluid book-image rounded" alt="${livre.titre}">
            </div>
            <div class="col-md-8">
                <h1 class="mb-3">${livre.titre}</h1>
                <h4 class="text-muted mb-4">par ${livre.auteur}</h4>
                
                <div class="mb-4">
                    <span class="badge bg-${livre.disponible ? 'success' : 'danger'} mb-2">
                        ${livre.disponible ? 'Disponible' : 'Emprunté'}
                    </span>
                    <span class="badge bg-secondary">${livre.categorie}</span>
                </div>

                <h5>Description</h5>
                <p class="lead">${livre.description}</p>

                <c:if test="${sessionScope.admin != null}">
                    <div class="mt-4">
                        <a href="${pageContext.request.contextPath}/admin/edit?id=${livre.id}" class="btn btn-warning me-2">Modifier</a>
                        <button onclick="confirmDelete(${livre.id})" class="btn btn-danger">Supprimer</button>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Modal de confirmation de suppression -->
    <div class="modal fade" id="deleteModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirmer la suppression</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    Êtes-vous sûr de vouloir supprimer ce livre ?
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                    <a id="deleteLink" href="" class="btn btn-danger">Supprimer</a>
                </div>
            </div>
        </div>
    </div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>    <script>
        function confirmDelete(id) {
            // Mettre à jour le lien de suppression avec l'ID du livre
            document.getElementById('deleteLink').href = '${pageContext.request.contextPath}/admin/supprimer?id=' + id;
            // Afficher la modal
            const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
            modal.show();
        }
    </script>
</body>
</html>
