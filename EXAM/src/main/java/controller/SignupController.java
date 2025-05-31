package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SignupController {


    @FXML private TextField usernameField;
    @FXML private TextField vocalField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> authModeComboBox;
    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    @FXML private Text infoText;
    @FXML private Text errorText;

    @FXML
    private void initialize() {
        authModeComboBox.getItems().setAll("PASSWORD_ONLY", "VOICE_ONLY", "BOTH");
    }

    @FXML
    private void handleSignup() {
        String username = usernameField.getText();
        String vocal = vocalField.getText();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();
        String selectedAuthMode = authModeComboBox.getValue();

        errorText.setText("");
        infoText.setText("");

        if (username.isEmpty() || selectedAuthMode == null) {
            errorText.setText("Veuillez entrer un nom d'utilisateur et sélectionner un mode d'authentification.");
            return;
        }

        User.AuthMode authMode = User.AuthMode.valueOf(selectedAuthMode);

        // Vérification conditionnelle des champs selon le mode choisi
        if (authMode == User.AuthMode.PASSWORD_ONLY) {
            if (password.isEmpty() || confirm.isEmpty()) {
                errorText.setText("Veuillez entrer et confirmer votre mot de passe.");
                return;
            }
            if (!password.equals(confirm)) {
                errorText.setText("Les mots de passe ne correspondent pas.");
                return;
            }
        } else if (authMode == User.AuthMode.VOICE_ONLY) {
            if (vocal.isEmpty()) {
                errorText.setText("Veuillez entrer votre phrase vocale.");
                return;
            }
        } else if (authMode == User.AuthMode.BOTH) {
            if (password.isEmpty() || confirm.isEmpty() || vocal.isEmpty()) {
                errorText.setText("Veuillez remplir le mot de passe, la confirmation et la phrase vocale.");
                return;
            }
            if (!password.equals(confirm)) {
                errorText.setText("Les mots de passe ne correspondent pas.");
                return;
            }
        }

        UserDatabase db = UserDatabase.loadOrCreate();

        if (db.getUser(username) != null) {
            infoText.setText("Utilisateur déjà présent.");
        } else {
            if (authMode == User.AuthMode.PASSWORD_ONLY) {
                db.addUser(username, password, null, authMode);
            } else if (authMode == User.AuthMode.VOICE_ONLY) {
                db.addUser(username, null, vocal, authMode);
            } else { // BOTH
                db.addUser(username, password, vocal, authMode);
            }

            db.save(); // sauvegarder
            infoText.setText("Compte créé avec succès !");
            clearFields();
        }
    }

    private void clearFields() {
        usernameField.clear();
        vocalField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        authModeComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleGoToLogin(ActionEvent event) {
        // Redirection vers la vue de connexion
        try {
            // Tente de charger la vue de login
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));

            // Obtenir le stage actuel via le bouton qui a déclenché l'action
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle (sign up)
            currentStage.setScene(new Scene(loginRoot,400, 300));
            currentStage.setTitle("Sign in");
            currentStage.show();

        } catch (IOException e) {
            // Si une erreur survient lors du chargement du FXML, on affiche une erreur
            System.err.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de la fenêtre principale", e);

        }
    }
}
