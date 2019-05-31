package br.edu.insper.al.matheusp1.projeto2.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private final String displayName;

    public LoggedInUser(String userId, String displayName) {
        String userId1 = userId;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
