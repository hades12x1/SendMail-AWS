package com.adflex.controller;

import com.adflex.model.Message;
import com.adflex.sentmail.MailService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ControllerEmail {
    @Autowired
    private MailService mailService;

    @PostMapping("/sent-mail")
    public ResponseEntity<String> sentMail(@RequestBody Message message) {
        Document mailConfig = message.getDocument();
        try {
            mailService.handleMessage(mailConfig);
            return ResponseEntity.ok("Ok");
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ExceptionUtils.getStackTrace(ex));
        }
    }
}
