package com.naveen.learning.services.impl;

import com.naveen.learning.dto.Mail;
import com.naveen.learning.services.MailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {

    private static final Logger logger = Logger.getLogger(MailServiceImpl.class);
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Configuration templateConfiguration;

    @Value("${app.velocity.templates.location}")
    private String templatePath;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${app.reset.password.token.duration}")
    private Long expiration;


    private void sendMail(Mail mail) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(mail.getTo());
            helper.setFrom(mail.getFrom());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent(), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Error Occurred in sendMail()");
            throw new RuntimeException(e);
        }
    }

    //Mail for account activity
    @Override
    public void sendAccountChangesEmail(String action, String status, String toEmail) {
        Mail mail = new Mail();
        mail.setTo(toEmail);
        mail.setFrom(mailFrom);
        mail.setSubject("Account Status Change");
        Map<String, String> model = new HashMap<>();
        model.put("userName", toEmail);
        model.put("action", action);
        model.put("actionStatus", status);
        mail.setModel(model);

        try {
            templateConfiguration.setClassForTemplateLoading(getClass(), templatePath);
            Template template = templateConfiguration.getTemplate("account-activity-change.ftl");
            String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            mail.setContent(mailContent);
            sendMail(mail);
        } catch (TemplateException | IOException e) {
            System.out.println("Account activity mail send failed");
            logger.error("Error Occurred while sending a Account change activity mail: " + e.getMessage());
            throw new RuntimeException(e);
        }


    }

    //Mail for Password Reset
    @Override
    public void sendResetLink(String emailConfirmationUrl, String toMail) {
Long expirationInMinutes= TimeUnit.MICROSECONDS.toMinutes(expiration);
        Mail mail = new Mail();
        mail.setTo(toMail);
        mail.setFrom(mailFrom);
        mail.setSubject("Password Reset Link");
        Map<String, String> model = new HashMap<>();
        model.put("userName", toMail);
        model.put("userResetPasswordLink", emailConfirmationUrl);
        model.put("expirationTime", expirationInMinutes.toString());
        mail.setModel(model);

        try {
            templateConfiguration.setClassForTemplateLoading(getClass(), templatePath);
            Template template = templateConfiguration.getTemplate("password-reset.ftl");
            String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            mail.setContent(mailContent);
            sendMail(mail);
        } catch (TemplateException | IOException e) {
            System.out.println("Reset Link mail send failed");
            logger.error("Error Occurred while sending a Password Reset mail: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
