package com.adflex.sentmail;

import org.apache.activemq.Message;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class JobDequeueMail {
    private static final Logger LOG = LoggerFactory.getLogger(JobDequeueMail.class);
    private static final ExecutorService EXECUTOR = Executors.newWorkStealingPool(10);

    @Autowired
    private MailService mailService;

    @JmsListener(destination = "${activemq.job.mail-queue}")
    void receiveMessage(final Message message) {
        Document messageDocument = this.parseDocument(message);
        EXECUTOR.submit(() -> {
            try {
                mailService.handleMessage(messageDocument);
            } catch (Exception ex) {
                LOG.error("JobDequeueMail error: ", ex.getCause());
            }
        });
    }

    private Document parseDocument(javax.jms.Message message) {
        try {
            if (message == null) {
                return null;
            }
            return Document.parse(((ActiveMQTextMessage) message).getText());
        } catch (Exception ex) {
            return null;
        }
    }
}
