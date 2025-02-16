package ci.cssr;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);  // Méthode pour trouver un utilisateur par email
}
