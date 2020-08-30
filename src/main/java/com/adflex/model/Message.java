package com.adflex.model;

import org.bson.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Message {
    private String from;
    private String fromName;
    private String body;
    private List<String> attachments;
    private List<String> to;
    @NotNull(message = "Field subject is not blank")
    private String subject;

    public Message() {
    }

    public Message(String from, String fromName, String body, List<String> attachments, List<String> to, String subject) {
        this.from = from;
        this.fromName = fromName;
        this.body = body;
        this.attachments = attachments;
        this.to = to;
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Document getDocument() {
        Document message = new Document();
        message.put("from", from);
        message.put("from_name", fromName);
        message.put("body", body);
        message.put("attachments", attachments);
        message.put("to", to);
        message.put("subject", subject);
        return message;
    }
}
