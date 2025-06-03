package com.oceane.surveys.services;

import com.oceane.surveys.entities.Recipient;
import com.oceane.surveys.entities.Survey;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.from:enquetes@oceaneconsulting.com}")
    private String fromEmail;

    /**
     * Envoie un email d'invitation pour participer à une enquête
     */
    public void sendSurveyInvitation(Survey survey, Recipient recipient, String surveyUrl) {
        try {
            log.info("Envoi d'email d'invitation à {}", recipient.getEmail());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(recipient.getEmail());
            helper.setSubject("Enquête de satisfaction - " + survey.getTitle());

            String content = buildSurveyEmailContent(survey, recipient, surveyUrl);
            helper.setText(content, true); // true pour le contenu HTML

            mailSender.send(message);
            log.info("Email envoyé avec succès à {}", recipient.getEmail());
        } catch (MessagingException e) {
            log.error("Erreur lors de l'envoi de l'email à {}: {}", recipient.getEmail(), e.getMessage());
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }

    /**
     * Construit le contenu HTML de l'email d'invitation avec le lien d'enquête
     */
    private String buildSurveyEmailContent(Survey survey, Recipient recipient, String surveyUrl) {
        return "<html><body>" +
                "<h2>Enquête de satisfaction - " + survey.getTitle() + "</h2>" +
                "<p>Bonjour " + recipient.getFirstName() + ",</p>" +
                "<p>Merci de prendre quelques minutes pour répondre à cette enquête.<br/>" +
                "Vos réponses nous aideront à améliorer nos services, renforcer notre accompagnement et mieux répondre à vos attentes.</p>" +
                "<div style='margin:20px;margin-left:100px;'>" +
                "<a href='" + surveyUrl + "' style='background-color:#007bff; color:white; padding:10px 15px; " +
                "text-decoration:none; border-radius:4px;'>Répondre à l'enquête</a></div>" +
                "<p>Si le bouton ne fonctionne pas, veuillez copier et coller ce lien dans votre navigateur:<br/>" +
                "<a href='" + surveyUrl + "'>" + surveyUrl + "</a></p>" +
                "<p>Cordialement,<br/>L'équipe Oceane Consulting</p>" +
                "<div style='margin-top:5px;'>" +
                "<img src='https://www.oceaneconsulting.com/wp-content/uploads/logo-oceane-consulting-bleu-gris.png' alt='Oceane Consulting Logo' width='200'/>" +
                "</div>" +
                "</body></html>";
    }
}