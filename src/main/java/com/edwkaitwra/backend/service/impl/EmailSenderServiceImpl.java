package com.edwkaitwra.backend.service.impl;

import com.edwkaitwra.backend.domain.Email;
import com.edwkaitwra.backend.model.EmailModel;
import com.edwkaitwra.backend.repo.EmailRepo;
import com.edwkaitwra.backend.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    private EmailRepo emailRepo;

    @Override
    public void sendHtmlMessage(EmailModel emailModel) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(emailModel.getProperties());
        helper.setFrom(emailModel.getFrom());
        helper.setTo(emailModel.getTo());
        helper.setSubject(emailModel.getSubject());
        String html = templateEngine.process(emailModel.getTemplate(), context);
        helper.setText(html, true);
        log.info("Sending emailModel: {} with html body: {}", emailModel, html);
        emailSender.send(message);
        Email email = new Email();
        email.setSentAt(LocalDateTime.now());
        email.setHtml(html);
        email.setReceiver(emailModel.getTo());
        email.setSender(emailModel.getFrom());
        emailRepo.save(email);
    }
}
