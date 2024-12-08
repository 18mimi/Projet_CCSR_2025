package ci.cssr;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Scanner;

public class producer{

    public static void main(String[] args) {
        // Configurations pour le producteur
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); // Adresse du serveur Kafka
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // Utilisateur entre les messages dans la console
        Scanner scanner = new Scanner(System.in);
        String topic = "my_topic"; // Remplacez par votre topic
        System.out.println("Entrez des messages à envoyer à Kafka (tapez 'exit' pour quitter) :");

        while (true) {
            System.out.print("> ");
            String message = scanner.nextLine();
            if ("exit".equalsIgnoreCase(message)) {
                break; // Quitte la boucle si l'utilisateur tape "exit"
            }
            producer.send(new ProducerRecord<>(topic, message));
            System.out.println("Message envoyé : " + message);
        }

        producer.close();
        scanner.close();
        System.out.println("Producteur fermé.");
    }
}
