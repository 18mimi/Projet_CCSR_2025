package ci.cssr;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    List<Preference> findByUserEmail(String email);
}
