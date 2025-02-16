package ci.cssr;

public class Message {
    private String to;
    private String subject;
    private String content;

    // Constructeur
    public Message(String to, String subject, String content) {
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    // Getters
    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }
}
