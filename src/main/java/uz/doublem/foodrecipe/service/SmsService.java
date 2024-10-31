package uz.doublem.foodrecipe.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

public class SmsService {
    @Bean
    @SneakyThrows
    public void sendSmsToUser(String email, String text) {
        String subject = String.format("<h1>this is code to confirm your account<a href=https://kun.uz> %s<a/>, please do not share this code with anyone</h1>", text);
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("abror01042001@gmail.com", "tnpfvoapprujggkk");
            }
        };
        Session session = Session.getInstance(properties, auth);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("abror01042001@gmail.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject(text);

        message.setContent(subject, "text/html");

        Transport.send(message);

    }
}
