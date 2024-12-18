<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${livre == null ? 'Ajouter' : 'Modifier'} un livre - Bibliothèque</title>
     <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="../header.jsp" />

    <div class="container py-5">
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/livres">Gestion des Livres</a></li>
                <li class="breadcrumb-item active">${livre == null ? 'Ajouter' : 'Modifier'} un livre</li>
            </ol>
        </nav>

        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h4 class="mb-0">${livre == null ? 'Ajouter' : 'Modifier'} un livre</h4>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/admin/${livre == null ? 'save' : 'update'}" 
                              method="post" 
                              enctype="multipart/form-data">
                            
                            <c:if test="${livre != null}">
                                <input type="hidden" name="id" value="${livre.id}">
                            </c:if>
                            
                            <div class="mb-3">
                                <label for="titre" class="form-label">Titre</label>
                                <input type="text" class="form-control" id="titre" name="titre" 
                                       value="${livre.titre}" required>
                            </div>

                            <div class="mb-3">
                                <label for="auteur" class="form-label">Auteur</label>
                                <input type="text" class="form-control" id="auteur" name="auteur" 
                                       value="${livre.auteur}" required>
                            </div>

                            <div class="mb-3">
                                <label for="categorie" class="form-label">Catégorie</label>
                                <select class="form-select" id="categorie" name="categorie" required>
                                    <option value="">Sélectionner une catégorie</option>
                                    <option value="Roman" ${livre.categorie == 'Roman' ? 'selected' : ''}>Roman</option>
                                    <option value="Science" ${livre.categorie == 'Science' ? 'selected' : ''}>Science</option>
                                    <option value="Histoire" ${livre.categorie == 'Histoire' ? 'selected' : ''}>Histoire</option>
                                    <option value="Informatique" ${livre.categorie == 'Informatique' ? 'selected' : ''}>Informatique</option>
                                    <option value="Litterature" ${livre.categorie == 'Litterature' ? 'selected' : ''}>Littérature</option>
                                    <option value="Romance" ${livre.categorie == 'Romance' ? 'selected' : ''}>Romance</option>       
                                    <option value="Fantasy" ${livre.categorie == 'Fantasy' ? 'selected' : ''}>Fantasy</option>
                                    
                                    
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label">Description</label>
                                <textarea class="form-control" id="description" name="description" 
                                          rows="4" required>${livre.description}</textarea>
                            </div>

                            <div class="mb-3">
                                <label for="image" class="form-label">Image</label>
                                <input type="file" class="form-control" id="image" name="image" 
                                       accept="image/*" ${livre == null ? 'required' : ''}>
                                <c:if test="${livre != null && livre.imageUrl != null}">
                                    <div class="mt-2">
                                        <small class="form-text text-muted">Image actuelle :</small>
                                        <img src="${pageContext.request.contextPath}/${livre.imageUrl}" 
                                             alt="Image actuelle" class="img-thumbnail mt-2" style="max-height: 100px;">
                                    </div>
                                </c:if>
                            </div>

                            <div class="mb-3">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="disponible" 
                                           name="disponible" ${livre == null || livre.disponible ? 'checked' : ''}>
                                    <label class="form-check-label" for="disponible">
                                        Disponible
                                    </label>
                                </div>
                            </div>

                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary">
                                    ${livre == null ? 'Ajouter' : 'Modifier'} le livre
                                </button>
                                <a href="${pageContext.request.contextPath}/admin/livres" class="btn btn-secondary">Annuler</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script></body>
</html>
