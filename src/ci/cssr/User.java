package ci.cssr;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "utilisateurs")  // Correspond au nom exact de la table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Identifiant unique de l'utilisateur

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Preference> preferences;

    @Column(nullable = false, unique = true)
    private String email;  // Adresse email de l'utilisateur

    @Column(name = "date_inscription", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp dateInscription;  // Date d'inscription de l'utilisateur

    // Constructeur sans argument (obligatoire pour JPA)
    public User() {}

    // Constructeur avec paramètres
    public User(String email) {
        this.email = email;
        this.dateInscription = new Timestamp(System.currentTimeMillis());
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Timestamp dateInscription) {
        this.dateInscription = dateInscription;
    }
}
