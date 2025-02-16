package ci.cssr;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.TimeoutException;

import java.util.Properties;
import java.util.Scanner;

class Producer {
    private KafkaProducer<String, String> producer;
    private String publisherId;

    // Constructeur
    public Producer(String publisherId) {
        this.publisherId = publisherId;

        // Configuration du producteur Kafka
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // Désactivation de l'idempotence et autres configurations pour simplifier les tests
        props.put("acks", "all"); // Attente d'un ack de tous les réplicas
        props.put("retries", 0); // Pas de nouvelle tentative en cas d'échec
        props.put("linger.ms", 1); // Optimisation des envois de messages pour réduire les délais

        this.producer = new KafkaProducer<>(props);
    }

    // Méthode pour publier un message saisi dans la console
    public void startPublishing() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==== Bienvenue, " + publisherId + " ====");
        System.out.println("Saisissez vos messages à publier dans Kafka.");

        while (true) {
            System.out.println("\nEntrez le titre du message (ou tapez 'exit' pour quitter) :");
            String title = scanner.nextLine();
            if ("exit".equalsIgnoreCase(title)) {
                break;
            }

            System.out.println("Entrez le contenu du message :");
            String content = scanner.nextLine();

            System.out.println("Entrez les mots-clés séparés par des virgules :");
            String keywords = scanner.nextLine();

            // Construire le message JSON
            String message = "{"
                    + "\"publisherId\": \"" + this.publisherId + "\","
                    + "\"keywords\": [\"" + String.join("\", \"", keywords.split(",")) + "\"],"
                    + "\"title\": \"" + title + "\","
                    + "\"content\": \"" + content + "\""
                    + "}";

            // Publier le message dans un topic général (central)
            ProducerRecord<String, String> record = new ProducerRecord<>("content-topic", null, message);

            try {
                producer.send(record, (metadata, exception) -> {
                    if (exception != null) {
                        System.out.println("Erreur lors de l'envoi du message : " + exception.getMessage());
                    } else {
                        System.out.println("\nMessage publié avec succès !");
                        System.out.println("Titre : " + title);
                        System.out.println("Contenu : " + content);
                        System.out.println("Mots-clés : " + keywords);
                    }
                });

            } catch (TimeoutException e) {
                System.out.println("Erreur de connexion ou délai d'attente dépassé : " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Une erreur inattendue est survenue : " + e.getMessage());
            }
        }

        scanner.close();
        producer.close();
        System.out.println("Session terminée. Merci d'avoir utilisé le Publisher Console !");
    }

    public static void main(String[] args) {
        // Créer une instance du publisher
        Producer publisher = new Producer("publisher1");
        publisher.startPublishing();
    }
}
