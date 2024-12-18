<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bibliothèque - Accueil</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css" rel="stylesheet">
<%--     <link href="${pageContext.request.contextPath}/BS/bootstrap.min.css" rel="stylesheet">
 --%> 
     <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
 
    <style>
        .book-card {
            height: 100%;
            display: flex;
            flex-direction: column;
        }
        .card-body {
            flex: 1;
            display: flex;
            flex-direction: column;
        }
        .book-info {
            flex: 1;
        }
        .card-footer {
            background: none;
            border-top: none;
            padding-top: 0;
            margin-top: auto;
        }
        .book-image {
            height: 200px;
            object-fit: cover;
        }
        .description-truncate {
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            text-overflow: ellipsis;
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container py-5">
        <!-- Barre de recherche -->
        <div class="row mb-4">
            <div class="col-md-6 mx-auto">
                <form action="${pageContext.request.contextPath}/chercher" method="get" class="d-flex">
                    <input type="text" name="motCle" class="form-control me-2" 
                           placeholder="Rechercher un livre..." value="${motCle}">
                    <button type="submit" class="btn btn-primary">Rechercher</button>
                </form>
            </div>
        </div>

        <!-- Liste des livres -->
        <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
            <c:forEach items="${livres}" var="livre">
                <div class="col">
                    <div class="card h-100 book-card">
                        <c:if test="${not empty livre.imageUrl}">
                            <img src="${livre.imageUrl}" 
                                 class="card-img-top book-image" alt="${livre.titre}"
                                 onerror="this.src='../images/default-book.jpg'">
                        </c:if>
                        <div class="card-body">
                            <div class="book-info">
                                <h5 class="card-title">${livre.titre}</h5>
                                <h6 class="card-subtitle mb-2 text-muted">${livre.auteur}</h6>
                                <p class="card-text description-truncate">${livre.description}</p>
                            </div>
                        </div>
                           <div class="card-footer bg-light d-flex justify-content-between align-items-center pt-2 "
                           			>
			                    <!-- Badge -->
			                    <span class="badge bg-${livre.disponible ? 'success' : 'danger'}">
			                        ${livre.disponible ? 'Disponible' : 'Emprunté'}
			                    </span>
			                    <!-- Bouton -->
			                    <a href="${pageContext.request.contextPath}/details?id=${livre.id}" 
			                       class="btn btn-primary btn-sm">Voir plus</a>
			                </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Pagination -->
        <c:if test="${not empty totalPages and totalPages > 1}">
            <div class="row mt-4">
                <div class="col">
                    <nav aria-label="Navigation des pages">
                        <ul class="pagination justify-content-center">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/?page=${currentPage - 1}">Précédent</a>
                            </li>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/?page=${i}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/?page=${currentPage + 1}">Suivant</a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </c:if>
    </div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script></body>
</html>
