package pl.tazz.ideas.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class EmailSendService {

    private static final Logger logger = LoggerFactory.getLogger(EmailSendService.class);

    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailSendService(JavaMailSender mailSender, MessageSource messageSource) {
        this.mailSender = mailSender;
        this.messageSource = messageSource;
    }

    public void sendVerificationEmail(String toEmail, String token) {
        try {
            String verificationUrl = "http://localhost:8080/verify-email?token=" + token;

            String subject = messageSource.getMessage("email.verification.subject", null, Locale.getDefault());
            String text = messageSource.getMessage("email.verification.text", new Object[]{verificationUrl}, Locale.getDefault());

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            logger.info("E-mail weryfikacyjny wysłany do: {}", toEmail);
        } catch (MailException ex) {
            logger.error("Błąd podczas wysyłania e-maila weryfikacyjnego do: {}. Szczegóły: {}", toEmail, ex.getMessage(), ex);
        }
    }
}
