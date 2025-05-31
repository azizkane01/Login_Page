package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.io.IOException;
import javafx.scene.control.Label;
public class MainController {
    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        String username = LoginController.text; // récupére dynamiquement le nom d'utilisateur
        welcomeLabel.setText("Bienvenue, " + username + " !");
    }

    @FXML
    private void  Deconnection(ActionEvent event) {
        try {
            // Tente de charger la vue de login
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));

            // Obtenir le stage actuel via le bouton qui a déclenché l'action
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle (login)
            currentStage.setScene(new Scene(loginRoot,400, 300));
            currentStage.setTitle("Login");
            currentStage.show();

        } catch (IOException e) {
            // Si une erreur survient lors du chargement du FXML, on affiche une erreur
            System.err.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
