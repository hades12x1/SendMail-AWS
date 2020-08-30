package com.adflex.sentmail;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service
public class MailService {
    private static final Logger LOG = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private Transport transportMail;

    @Autowired
    private Session sessionMail;

    @Value("${mail.client.default.from}")
    private String defaultFrom;

    @Value("${mail.client.default.name}")
    private String defaultName;

    @Value("${mail.amazone.ses.username}")
    private String sesUsername;

    @Value("${mail.amazone.ses.password}")
    private String sesPassword;

    @Value("${mail.amazone.ses.host}")
    private String sesHost;

    public void handleMessage(final Document messageDocument) throws Exception {
        if (this.invalidEmail(messageDocument)) {
            MimeMessage mail = this.createMailFromMessage(messageDocument);
            if (!transportMail.isConnected()) {
                transportMail.connect(sesHost, sesUsername, sesPassword);
            }
            System.out.println("Sent message success: " + messageDocument.toJson());
            transportMail.sendMessage(mail, mail.getAllRecipients());
            return;
        }
        LOG.warn("Email invalid format: " + messageDocument.toJson());
    }

    private MimeMessage createMailFromMessage(Document messageDocument) throws Exception {
        String from = (String) messageDocument.getOrDefault("from", defaultFrom);
        String fromName = (String) messageDocument.getOrDefault("from_name", defaultName);
        String subject = messageDocument.getString("subject");
        String body = messageDocument.getString("body");

        Address[] recipients = this.getFieldToAddress(messageDocument);
        MimeMessage message = new MimeMessage(sessionMail);
        message.setFrom(new InternetAddress(from, fromName));
        message.addRecipients(RecipientType.TO, recipients);
        message.setSubject(subject, "UTF-8");

        MimeMultipart mimeMultipart = new MimeMultipart("alternative");
        MimeBodyPart wrap = new MimeBodyPart();
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(body, "text/html; charset=UTF-8");
        mimeMultipart.addBodyPart(htmlPart);
        wrap.setContent(mimeMultipart);

        MimeMultipart msg = new MimeMultipart("mixed");
        message.setContent(msg);
        msg.addBodyPart(wrap);
        this.attachmentFiles(messageDocument, msg);
        return message;
    }

    private void attachmentFiles(Document messageDocument, MimeMultipart msg) throws MalformedURLException, MessagingException {
        List<String> uriFiles = messageDocument.getList("attach_file", String.class);
        for (String uri : uriFiles) {
            URL url = new URL(uri);
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setDataHandler(new DataHandler(url));
            mimeBodyPart.setFileName(url.getFile());
            msg.addBodyPart(mimeBodyPart);
        }
    }

    private Address[] getFieldToAddress(Document messageDocument) throws Exception {
        List<String> toReceiver = messageDocument.getList("to", String.class);
        Address[] toAddress = new Address[toReceiver.size()];
        for (int i = 0; i < toReceiver.size(); i++) {
            toAddress[i] = new InternetAddress(toReceiver.get(i));
        }
        return toAddress;
    }

    private boolean invalidEmail(Document mailDocument) {
        if (mailDocument == null) {
            return false;
        }
        List<String> toRecipients = mailDocument.getList("to", String.class);
        boolean exitsFieldTo = CollectionUtils.isNotEmpty(toRecipients);
        String subject = mailDocument.getString("subject");
        return StringUtils.isNotBlank(subject) && exitsFieldTo;
    }
}
