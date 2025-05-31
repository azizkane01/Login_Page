package controller;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserDatabase implements Serializable {
    private static final String FILE_PATH = "users.db";
    private Map<String, User> users = new HashMap<>();

    public static UserDatabase loadOrCreate() { // cette fonction va charger le fichier users.db si elle existe sinon elle la crée
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (UserDatabase) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        UserDatabase db = new UserDatabase();
        db.addUser("admin", "admin", "ouvir", User.AuthMode.BOTH); //par defaut il n'y a qu'un seul utilisateur creer lors de l'instanciation
        db.save();
        return db;
    }

    public void save() {// elle permet de sauvegarder le fichier
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // ajouter un utilisateur
    public void addUser(String username, String passwordHash, String voicePrint, User.AuthMode mode) {
        users.put(username, new User(username, passwordHash, voicePrint, mode));
    }

   /* public boolean authenticate(String username, String password, String voicePrint) { // permet de faire l'authentification
        User user = users.get(username);
        if (user == null) return false;

        switch (user.getAuthMode()) { // elle nous permet de connaitre le mode d'authentification
            case PASSWORD_ONLY:
                return password != null && password.equals(user.getPassword()); // teste si le mot de passe n'est pas nul et que le mot de passe entrer est le bon
            case VOICE_ONLY:
                return voicePrint != null && voicePrint.equals(user.getVoicePrint()); // teste si le vocal n'est pas nul et que celle entrer est le bon
            case BOTH:
                return password != null && voicePrint != null &&      //// teste si le vocal et le password ne sont  pas nuls et que celle entrer est le bon
                        password.equals(user.getPassword()) &&
                        voicePrint.equals(user.getVoicePrint());
            default:
                return false;

    }*/

    public User getUser(String username) {
        return users.get(username);
    }
}
