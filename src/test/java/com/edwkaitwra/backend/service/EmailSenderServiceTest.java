package com.edwkaitwra.backend.service;

import com.edwkaitwra.backend.model.EmailModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
//@Ignore
public class EmailSenderServiceTest {

    @Autowired
    EmailSenderService emailSenderService;


    @Test
    public void sendHtmlMessageTest() throws MessagingException {
        EmailModel emailModel = new EmailModel();
        emailModel.setTo("skofield44@gmail.com");
        emailModel.setFrom("edwkaitwrateam@gmail.com");
        emailModel.setSubject("Welcome EmailModel from EdwKaiTwra");
        emailModel.setTemplate("welcome-email.html");
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", "Mom <3");
        properties.put("subscriptionDate", LocalDate.now().toString());
        properties.put("technologies", Arrays.asList("Java", "Angular", "Google Cloud"));
        emailModel.setProperties(properties);

        Assertions.assertDoesNotThrow(() -> emailSenderService.sendHtmlMessage(emailModel));
    }
}