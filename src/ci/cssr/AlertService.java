package ci.cssr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void createAlert(AlerteRequest request) {
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            user = new User(request.getEmail());
            userRepository.save(user);
        }

        Preference.Frequency frequency;
        try {
            frequency = Preference.Frequency.valueOf(request.getFrequency().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Fréquence invalide: " + request.getFrequency());
        }

        Preference preference = new Preference(user, request.getMotCle(), frequency);
        preferenceRepository.save(preference);

        kafkaTemplate.send("content-topic", user.getEmail() + " -> " + request.getMotCle());
    }
}
