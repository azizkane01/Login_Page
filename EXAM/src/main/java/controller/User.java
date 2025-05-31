package controller;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String voicePrint;
    private AuthMode authMode; // <-- nouveau champ

    public  enum AuthMode {
        PASSWORD_ONLY,
        VOICE_ONLY,
        BOTH
    }

    public User(String username, String password, String voicePrint, AuthMode authMode) {
        this.username = username;
        this.password = password;
        this.voicePrint = voicePrint;
        this.authMode = authMode;
    }

    // Getters & Setters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getVoicePrint() {
        return voicePrint;
    }

    public AuthMode getAuthMode() {
        return authMode;
    }

    public void setAuthMode(AuthMode authMode) {
        this.authMode = authMode;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVoicePrint(String voicePrint) {
        this.voicePrint = voicePrint;
    }
}
