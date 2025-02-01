import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;


public class EmailService {

    private static final String SMTP_HOST = "smtp.sendgrid.net"; // Remplace par le serveur SMTP de ton fournisseur
    private static final String SMTP_PORT = "587"; // Port pour TLS
    private static final String USERNAME = "api"; // Ton email
    private static final String API_KEY = "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"; // Ton mot de passe ou App Password

    public void router(String to, String subject, String content) {
        // Configurer les propriétés SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);

        // Session avec authentification
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, API_KEY);
            }
        });

        try {
            // Construire le message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("noreply@tondomaine.com")); // Adresse e-mail d'expéditeur (optionnel)
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            // Envoyer l'e-mail
            Transport.send(message);
            System.out.println("E-mail envoyé avec succès à : " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'envoi de l'e-mail à : " + to);
        }
    }

}
