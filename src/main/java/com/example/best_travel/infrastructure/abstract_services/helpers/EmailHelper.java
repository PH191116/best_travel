package com.example.best_travel.infrastructure.abstract_services.helpers;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Component
public class EmailHelper {
    private final JavaMailSender mailSender;
    private final Path PATH=Paths.get("src/main/resources/email/email_template.html");

    public void sendMail(String to, String name, String product){
        MimeMessage message = mailSender.createMimeMessage();
        String htmlContent = this.readHtmlTemplate(to, product);
        try {
            message.setFrom(new InternetAddress("michael696padilla@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, to);//ConCopia
            message.setContent(htmlContent, MediaType.TEXT_HTML_VALUE);
            mailSender.send(message);
        }catch (MessagingException ex){
            log.error("Error to send email", ex.getMessage());
        }
    }

    private String readHtmlTemplate(String name, String producto){
        try(var lines = Files.lines(PATH)) {
            var html = lines.collect(Collectors.joining());
            return html.replace("{name}", name).replace("{product}", producto);
        }catch (IOException io){
            log.error("Error to read html template", io.getMessage());
            throw new RuntimeException();
        }
    }

}
