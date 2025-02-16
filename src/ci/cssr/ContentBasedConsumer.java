package ci.cssr;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class ContentBasedConsumer {

    public static void main(String[] args) {
        // Configuration de Kafka Streams
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "content-based-consumer");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        StreamsBuilder builder = new StreamsBuilder();

        // Consommation des messages du topic "content-topic"
        KStream<String, String> messages = builder.stream("content-topic");

        // Traitement du contenu des messages
        messages.filter((key, value) -> value.contains("keyword")) // Filtrer les messages
                .foreach((key, value) -> {
                    // Traiter le message et rediriger en fonction du contenu
                    System.out.println("Message traité : " + value);
                    // Logique pour rediriger vers les abonnés
                });

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        // Ajouter un shutdown hook pour fermer proprement
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
