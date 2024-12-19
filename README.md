# Bibliotheque
Application web simple développée en Java EE permettant la gestion des livres, des emprunts au sein d'une bibliothèque.

# Mini Projet Java EE : Gestion d'une Bibliothèque

## Description du Projet

Ce projet consiste à développer une application web de gestion d'une bibliothèque en utilisant Java EE. L'application permet aux utilisateurs de consulter les livres disponibles, de les emprunter et de les retourner. Les bibliothécaires peuvent également ajouter, modifier ou supprimer des livres ainsi que gérer les emprunts et les comptes utilisateurs.

## Fonctionnalités Principales

### 1. Gestion des Livres
- **Ajouter un Livre** : Permet aux bibliothécaires d'ajouter de nouveaux livres au système.
- **Modifier un Livre** : Mise à jour des détails d'un livre existant.
- **Supprimer un Livre** : Retirer un livre du système.

### 2. Consultation des Livres
- **Rechercher des Livres** : Recherche par titre, auteur ou genre.
- **Visualiser la Liste des Livres** : Affichage de tous les livres disponibles avec leurs détails.

### 3. Emprunt de Livres
- **Emprunter un Livre** : Les utilisateurs peuvent emprunter des livres disponibles.
- **Historique des Emprunts** : Consultation de l'historique des emprunts incluant les dates de retour.

### 4. Retour de Livres
- **Retourner un Livre** : Les utilisateurs peuvent retourner les livres empruntés.

## Technologies Utilisées

- **Langage** : Java EE
- **Frontend** : JSP
- **Backend** : Servlets, JDBC
- **Base de Données** : MySQL
- **Serveur d'Applications** : Apache Tomcat version 10
- **IDE** : Eclipse

## Installation

### Prérequis

- **Java JDK 23 ou supérieur**
- **Java JRE 17 ou supérieur**
- **Apache Tomcat 10 ou supérieur** (ou autre serveur d'applications compatible Java EE)
- **MySQL** (ou autre SGBD)

### Étapes d'Installation

1. **Cloner le Répertoire**
   git clone https://github.com/ymabchour/Bibliotheque/tree/master

Configurer la Base de Données

Créez une base de données MySQL nommée db_bibliotheque.
Importez les scripts SQL présents dans le dossier sql/ pour créer les tables nécessaires.
Configurer le Fichier de Propriétés

Modifiez le fichier src/main/resources/config.properties pour y ajouter les informations de connexion à la base de données.
Déployer l'Application

Importez le projet dans votre IDE préféré.
Construisez le projet avec Maven :


Ouvrez votre navigateur et allez à l'adresse : http://localhost:8081/Bibliotheque/index

identifiant : admin@bibliotheque.com

mot de passe : admin123

S'inscrire ou se connecter pour consulter, emprunter et retourner des livres.
Bibliothécaires :
Accéder à l'interface d'administration pour gérer les livres et les utilisateurs.

### Auteur

Youssef Mabchour
https://www.linkedin.com/in/mabchour-youssef
ysf.mabchour@gmail.com
