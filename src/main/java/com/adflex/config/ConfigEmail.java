package com.adflex.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;

@Configuration
public class ConfigEmail {

    @Value("${mail.amazone.ses.port}")
    private Integer sesPort;

    @Value("${mail.amazone.ses.encryption}")
    private String sesCryption;

    @Value("${mail.amazone.ses.protocol}")
    private String sesProtocol;

    @Bean
    public Session createSession() {
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", sesProtocol);
        props.put("mail.smtp.port", sesPort);
        props.put("mail.smtp.starttls.enable", sesCryption);
        props.put("mail.smtp.auth", "true");
        return Session.getDefaultInstance(props);
    }

    @Bean
    public Transport createTransport() throws Exception {
        Session session = this.createSession();
        return session.getTransport();
    }
}
