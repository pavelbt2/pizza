package com.pizza.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    private String host = "localhost";

    //TODO @Value("${email.from}")
    private String from = "primo.pizza.app@exlibrisgroup.com";

    private String[] to = {"pavelbt2@gmail.com" ,"meravkr@gmail.com"};

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        return javaMailSender;
    }

    @Bean
    @Scope("prototype") // new instance each time
    public SimpleMailMessage simpleMailMessage() {
       SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
       simpleMailMessage.setFrom(from);
       simpleMailMessage.setTo(to);
       return simpleMailMessage;
    }
}
