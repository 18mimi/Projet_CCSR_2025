package ci.cssr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class Router {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/alert_system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        // Construire le flux Kafka Streams
        StreamsBuilder builder = new StreamsBuilder();

        // Lire les messages du topic "content-topic"
        KStream<String, String> stream = builder.stream("content-topic");

        // Processus pour extraire les mots-clés, trouver les abonnés et rediriger les messages
        stream.foreach((key, value) -> {
            System.out.println("Message reçu : " + value);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(value);
                String publisherId = jsonNode.get("publisherId").asText();
                String title = jsonNode.get("title").asText();
                String content = jsonNode.get("content").asText();
                System.out.println("Publisher ID: " + publisherId);
                System.out.println("Title: " + title);
                System.out.println("Content: " + content);

                List<String> keywords = extractKeywords(value);
                List<String> emails = findSubscribersByKeywords(keywords);
                sendMessagesToSubscribers(value, emails);
            } catch (IOException e) {
                System.err.println("Erreur lors du parsing du JSON : " + e.getMessage());
            }
        });

        // Créer et démarrer l'application Kafka Streams
        KafkaStreams streams = new KafkaStreams(builder.build(), KafkaStreamsConfig.getStreamProperties());
        streams.start();

        // Ajouter un hook pour fermer l'application proprement
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    // Extraire les mots-clés du message JSON
    private static List<String> extractKeywords(String jsonMessage) {
        List<String> keywords = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonMessage);

            JsonNode tagsNode = rootNode.path("keywords");
            if (tagsNode.isArray()) {
                for (JsonNode keyword : tagsNode) {
                    keywords.add(keyword.asText());
                }
            }

            String content = rootNode.path("content").asText();
            if (!content.isEmpty()) {
                String[] words = content.split("\\s+");
                for (String word : words) {
                    word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
                    if (!word.isEmpty() && !keywords.contains(word)) {
                        keywords.add(word);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du parsing du JSON : " + e.getMessage());
        }

        return keywords;
    }

    // Trouver les emails des abonnés en fonction des mots-clés
    private static List<String> findSubscribersByKeywords(List<String> keywords) {
        List<String> emails = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT u.email " +
                        "FROM utilisateurs u " +
                        "JOIN preferences p ON u.id = p.utilisateur_id " +
                        "WHERE p.mot_cle IN ("
        );

        for (int i = 0; i < keywords.size(); i++) {
            sql.append("?");
            if (i < keywords.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < keywords.size(); i++) {
                statement.setString(i + 1, keywords.get(i));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    emails.add(resultSet.getString("email"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emails;
    }

    // Envoi des messages aux abonnés
    private static void sendMessagesToSubscribers(String message, List<String> emails) {
        EmailService emailService = new EmailService();

        for (String email : emails) {
            new Thread(() -> {
                try {
                    String subject = "Nouvelle alerte correspondant à vos préférences";
                    String content = "Bonjour,\n\nVoici une nouvelle alerte qui pourrait vous intéresser :\n\n" +
                            message + "\n\nCordialement,\nL'équipe Alerts System";

                    Message customMessage = new Message(email, subject, content);
                    emailService.router(customMessage);
                    System.out.println("Email envoyé à : " + email);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Erreur lors de l'envoi de l'email à " + email);
                }
            }).start();
        }
    }
}
