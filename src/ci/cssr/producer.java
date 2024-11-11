package ci.cssr;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

public class producer {
    public static void main(String[] args) {

        // Configuration des propriétés du producteur
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); // L'adresse de votre serveur Kafka
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // Création du producteur Kafka
        Producer<String, String> producer = new KafkaProducer<>(props);

        // Envoi d'un message
        producer.send(new ProducerRecord<String, String>("my_topic", "Hello, Kafka!"));

        System.out.println("Message envoyé !");

        // Fermeture du producteur
        producer.close();
    }
}
