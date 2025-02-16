package ci.cssr;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alerts")  // Définit la route de base pour ce contrôleur
public class AlertController {

    private final AlertService alertService;

    // Injection de dépendance via le constructeur
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    // Méthode qui gère les requêtes POST sur /alerts
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAlert(@RequestBody AlerteRequest request) {
        try {
            alertService.createAlert(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
