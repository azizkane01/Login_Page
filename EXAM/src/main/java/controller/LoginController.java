package controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.AudioRecognition;
import javafx.concurrent.Task;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import java.io.IOException;

public class LoginController {
    public static String text;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Text errorText;
    @FXML private Text text_vocal;
    @FXML private FontIcon voiceIcon;
    private final AudioRecognition audioRec  = new AudioRecognition();
    UserDatabase db = UserDatabase.loadOrCreate();
    String phrase;
    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());



    @FXML
    private void handleVoiceLoginButton() {
        User user = db.getUser(usernameField.getText());


        if (user == null) {
            errorText.setText("Utilisateur inconnu !");
            return;
        }

        switch (user.getAuthMode()) {
            case VOICE_ONLY:
                handleVoice(user, false); // test seulement voix
                break;
            case BOTH:
                handleVoice(user, true);  // test voix + mot de passe
                break;
            default:
                errorText.setText("Le mode vocal n’est pas autorisé pour cet utilisateur.");
        }
    }


    @FXML
    private void handleLoginButton() {
        User user = db.getUser(usernameField.getText());

        if (user == null) {
            errorText.setText("Utilisateur inconnu !");
            return;
        }

        if (user.getAuthMode() == User.AuthMode.PASSWORD_ONLY || user.getAuthMode() == User.AuthMode.BOTH) {
            checkPassword(user); // test mot de passe (et uniquement mot de passe si mode = PASSWORD_ONLY)
        } else {
            errorText.setText("L’authentification par mot de passe n’est pas autorisée pour cet utilisateur.");
        }
    }



    private void checkPassword(User user) {
        String pwd = passwordField.getText();
        if (user.getPassword().equals(pwd) && user.getAuthMode() == User.AuthMode.PASSWORD_ONLY) {
            loadMainWindow();
        } else {
            if(user.getAuthMode() == User.AuthMode.BOTH){
                errorText.setText("Veuiller d'abord donner le pass vocal");
            }else{
            errorText.setText("Mot de passe incorrect !");}
        }
    }

    /**
     * @param user               l’utilisateur à authentifier
     * @param checkPasswordToo   si true, on vérifie mot de passe + voix ; sinon que voix
     */
    private void handleVoice(User user, boolean checkPasswordToo) {
        // toggle enregistrement
        if (!audioRec.isRecording()) {
            audioRec.startRecording();
            voiceIcon.setIconCode(FontAwesomeSolid.MICROPHONE);
        } else {
            voiceIcon.setIconCode(FontAwesomeSolid.MICROPHONE_SLASH);
            byte[] data = audioRec.stopRecording();

            // task pour ne pas bloquer l'UI
            Task<Void> t = new Task<>() {
                @Override
                protected Void call() {
                    phrase = audioRec.transcribeAudio(data);
                    boolean voiceOk  = phrase != null && phrase.equalsIgnoreCase(user.getVoicePrint());
                    boolean passOk   = !checkPasswordToo || user.getPassword().equals(passwordField.getText());

                    if (voiceOk && passOk) {
                        Platform.runLater(() -> loadMainWindow());
                    } else {
                        Platform.runLater(() -> {
                            if (!voiceOk && checkPasswordToo) {
                                errorText.setText("Voix ou mot de passe incorrect !");
                            } else if (!voiceOk) {
                                errorText.setText("Reconnaissance vocale échouée !");
                            } else {
                                errorText.setText("Mot de passe incorrect !");
                            }
                        });
                    }
                    return null;
                }
            };
            new Thread(t).start();
        }
    }

    void loadMainWindow() {
        try {
            text=usernameField.getText();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Application Principale");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

            // Fermer la fenêtre actuelle
            ((Stage) usernameField.getScene().getWindow()).close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de la fenêtre principale", e);

        }
    }
    @FXML
    private void SignupWindow(ActionEvent event) {
        try {
            // Tente de charger la vue de login
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/fxml/signup.fxml"));

            // Obtenir le stage actuel via le bouton qui a déclenché l'action
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle (sign up)
            currentStage.setScene(new Scene(loginRoot,450, 400));
            currentStage.setTitle("Sign up");
            currentStage.show();

        } catch (IOException e) {
            // Si une erreur survient lors du chargement du FXML, on affiche une erreur
            System.err.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de la fenêtre principale", e);

        }
    }


}
