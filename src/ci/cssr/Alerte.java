package ci.cssr;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "preferences")
public class Alerte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private User user;  // Assuming User is another entity representing users

    private String motCle;  // Corresponds to 'mot_cle' in your SQL schema

    @Enumerated(EnumType.STRING) // Ensures the enum is stored as a string
    private Frequency frequence;  // Corresponds to 'frequence' in your SQL schema

    @Column(name = "date_creation")
    private Timestamp createdAt;  // Changed to Timestamp for consistency with the SQL schema

    // Constructeur sans argument (exigé par JPA)
    public Alerte() {}

    // Constructeur avec paramètres
    public Alerte(User user, String motCle, Frequency frequence) {
        this.user = user;
        this.motCle = motCle;
        this.frequence = frequence;
    }

    // Getters et Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMotCle() {
        return motCle;
    }

    public void setMotCle(String motCle) {
        this.motCle = motCle;
    }

    public Frequency getFrequence() {
        return frequence;
    }

    public void setFrequence(Frequency frequence) {
        this.frequence = frequence;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // Enum Frequency
    public enum Frequency {
        QUOTIDIEN,
        HEBDOMADAIRE,
        MENSUEL
    }
}
