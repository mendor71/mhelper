package ru.pack.csps.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class MailService {
    private String host;
    private String port;
    private String techUserName;
    private String techPassword;
    private String techAddress;

    public void send(String to, String subject, String messageText) throws MessagingException {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(techUserName, techPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(techAddress));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(messageText, "text/html; charset=UTF-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setTechUserName(String techUserName) {
        this.techUserName = techUserName;
    }

    public void setTechPassword(String techPassword) {
        this.techPassword = techPassword;
    }

    public void setTechAddress(String techAddress) {
        this.techAddress = techAddress;
    }
}
