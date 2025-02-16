import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class MailTest {
    public static void main(String[] args) {
        // Configuration des propriétés SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");  // Hôte MailHog
        props.put("mail.smtp.port", "1025");       // Port SMTP de MailHog
        props.put("mail.smtp.auth", "false");      // Pas besoin d'authentification pour MailHog
        props.put("mail.smtp.starttls.enable", "false"); // Pas de TLS pour MailHog

        // Créer une session avec les propriétés définies
        Session session = Session.getInstance(props, null);

        try {
            // Créer un message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("yomi@example.com"));  // Adresse d'expéditeur
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("someone@example.com"));  // Destinataire
            message.setSubject("Test Email");  // Sujet de l'email
            message.setText("This is a test email.");  // Corps de l'email

            // Envoyer l'email
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
