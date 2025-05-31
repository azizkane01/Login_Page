# Projet d'Authentification Java Swing avec Reconnaissance Vocale

## Objectif pédagogique

Ce projet a pour but de concevoir une interface graphique en Java Swing permettant :

- L'affichage d'un formulaire d'authentification utilisateur.
- Un accès sécurisé par reconnaissance vocale.
- La manipulation des bibliothèques Swing et Audio en Java.
- L'intégration d'une bibliothèque open source de reconnaissance vocale (VOSK ou CMU Sphinx).
- Une fonctionnalité d'inscription (signup) ajoutée pour permettre la création de nouveaux utilisateurs.

---

## Description fonctionnelle

### 1. Fenêtre de connexion

- Champ texte pour le nom d'utilisateur.
- Champ mot de passe masqué.
- Bouton **Connexion** (classique).
- Bouton **Connexion vocale** déclenchant l'enregistrement audio.
- Zone de retour pour afficher les messages d'erreur, la transcription vocale, etc.
- Bouton **Inscription** (signup) pour créer un nouveau compte utilisateur.

### 2. Fenêtre d'inscription (signup)

- Champs pour nom d'utilisateur, mot de passe, confirmation du mot de passe.
- Validation simple des données.
- Stockage local  des utilisateurs inscrits.

### 3. Fenêtre principale

- Message de bienvenue avec le nom de l'utilisateur connecté.
- Menu factice pour simuler l'accès à une application.

---

## Comportement du bouton vocal

1. À l'appui, déclenchement de l'enregistrement via le micro (Java Sound API).
2. Utilisation de la bibliothèque de reconnaissance vocale (VOSK) pour convertir l'audio en texte.
3. Vérification de la transcription par rapport à une phrase-clé  (ex : "bonjour").
4. En cas de succès, l'utilisateur est authentifié automatiquement.

---

## Technologies utilisées

- **Java Swing** pour l'interface graphique.
- **Java Sound API** pour la capture audio.
- **VOSK API**  pour la reconnaissance vocale.


---

## Consignes de sécurité

- Le mot de passe vocal est une phrase simple, non chiffrée (usage pédagogique uniquement).
- Aucun enregistrement vocal n'est conservé.

---

## Instructions pour lancer l'application

1. Cloner le dépôt :

```bash
git clone https://github.com/azizkane01/Login_Page.git
