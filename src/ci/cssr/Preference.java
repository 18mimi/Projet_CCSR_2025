package ci.cssr;

import javax.persistence.*;

@Entity
@Table(name = "preferences")
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private User user;

    @Column(name = "mot_cle", nullable = false)
    private String motCle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Frequency frequence;

    public Preference() {}

    public Preference(User user, String motCle, Frequency frequence) {
        this.user = user;
        this.motCle = motCle;
        this.frequence = frequence;
    }

    public enum Frequency {
        QUOTIDIEN, HEBDOMADAIRE, MENSUEL
    }

    public Long getId() { return id; }
    public String getMotCle() { return motCle; }
    public Frequency getFrequence() { return frequence; }
    public User getUser() { return user; }
}
