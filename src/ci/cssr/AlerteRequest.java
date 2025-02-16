package ci.cssr;

public class AlerteRequest {

    private String email;  // L'email de l'utilisateur (correspond à la table 'utilisateurs')
    private String motCle; // Préférence de l'utilisateur (correspond à 'mot_cle' dans la table 'preferences')
    private String frequency; // Fréquence de la notification ('quotidien', 'hebdomadaire', 'mensuel')

    // Constructeur
    public AlerteRequest(String email, String motCle, String frequency) {
        this.email = email;
        this.motCle = motCle;
        this.frequency = frequency;
    }

    // Getters et Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotCle() {
        return motCle;
    }

    public void setMotCle(String motCle) {
        this.motCle = motCle;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    // Méthode pour obtenir l'ID utilisateur à partir de l'email
    // Cela supposerait que tu as une méthode pour obtenir l'utilisateur basé sur l'email
    public User getUserByEmail(String email) {
        // Implémenter la logique pour récupérer l'utilisateur en fonction de l'email
        // Par exemple, cela pourrait être un appel à la base de données pour récupérer l'utilisateur.
        return new User(email); // C'est un exemple, il faudrait adapter cette méthode.
    }

    // Méthode pour convertir la fréquence en valeur de l'énumération 'Frequency' si nécessaire
    public Alerte.Frequency getFrequencyEnum() {
        switch (this.frequency.toLowerCase()) {
            case "quotidien":
                return Alerte.Frequency.QUOTIDIEN;
            case "hebdomadaire":
                return Alerte.Frequency.HEBDOMADAIRE;
            case "mensuel":
                return Alerte.Frequency.MENSUEL;
            default:
                throw new IllegalArgumentException("Fréquence invalide: " + this.frequency);
        }
    }
}
