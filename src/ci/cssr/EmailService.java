package ci.cssr;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;


public class EmailService {

       // Cette méthode prend maintenant un objet Message
    public void router(Message message) {
        // Configurer les propriétés SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "false"); // Pas d'authentification
        properties.put("mail.smtp.starttls.enable", "false"); // Pas de TLS en local
        properties.put("mail.smtp.host", "localhost"); // Serveur SMTP MailHog
        properties.put("mail.smtp.port", "1025"); // Port SMTP


        // Session avec authentification
        Session session = Session.getInstance(properties);

        try {
            // Construire le message MimeMessage
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress("ganongnidan@gmail.com"));  // Adresse e-mail d'expéditeur
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(message.getTo()));
            mimeMessage.setSubject(message.getSubject());
            mimeMessage.setText(message.getContent());

            // Envoyer l'e-mail
            Transport.send(mimeMessage);
            System.out.println("E-mail envoyé avec succès à : " + message.getTo());
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'envoi de l'e-mail à : " + message.getTo());
        }
    }
}
