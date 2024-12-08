package ci.cssr;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collections;
import java.util.Properties;
public class topic {
    public static void main(String[] args) {
        // Configurations de Kafka
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Remplacez par votre serveur Kafka

        try (AdminClient adminClient = AdminClient.create(config)) {
            // Définir un nouveau topic
            String topicName = "my_topic";
            int partitions = 1;
            short replicationFactor = 1;

            NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);

            // Créer le topic
            adminClient.createTopics(Collections.singletonList(newTopic));
            System.out.println("Topic '" + topicName + "' créé avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
