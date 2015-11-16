package com.harlyn.config;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.UnsupportedEncodingException;

/**
 * Created by wannabe on 16.11.15.
 */
@Configuration
public class BeanConfig {
    @Bean(name = "mailSender")
    public JavaMailSenderImpl mailSender(@Value("${mail.host}") String host,
                                         @Value("${mail.username}") String username,
                                         @Value("${mail.password}") String password
    ) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding("UTF-8");

        return mailSender;
    }

    @Bean(name = "templateConfirmCodeMessage")
    public SimpleMailMessage templateMessage(@Value("${mail.confirm.from}") String from,
                                             @Value("${mail.confirm.subject}") String subject) throws UnsupportedEncodingException {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setSubject(subject);

        return mailMessage;
    }

    @Bean(name = "velocityEngine")
    public VelocityEngine velocityEngine() {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

        return velocityEngine;
    }
}
