package com.naveen.learning.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.util.Properties;

@Configuration
@EnableAsync
public class MailConfig {

    @Value("${spring.mail.default-encoding}")
    private String mailDefaultEncoding;

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${spring.mail.port}")
    private Integer mailPort;

    @Value("${spring.mail.protocol}")
    private String mailProtocol;

    @Value("${spring.mail.debug}")
    private String mailDebug;

    @Value("${spring.mail.smtp.auth}")
    private String mailSmtpAuth;

    @Value("${spring.mail.smtp.starttls.enable}")
    private String mailSmtpStartTls;


    @Bean
    @Primary
    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfigurationFactoryBean() {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath("/templates/");
        return bean;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);
        mailSender.setPort(mailPort);
        mailSender.setDefaultEncoding(mailDefaultEncoding);

        Properties mailProperties = new Properties();
        mailProperties.put("mail.transport.protocol", mailProtocol);
        mailProperties.put("mail.debug", mailDebug);
        mailProperties.put("mail.smtp.auth", mailSmtpAuth);
        mailProperties.put("mail.smtp.starttls.enable", mailSmtpStartTls);

        mailSender.setJavaMailProperties(mailProperties);
        return mailSender;
    }
}
