package com.edwkaitwra.backend.service;

import com.edwkaitwra.backend.model.EmailModel;

import javax.mail.MessagingException;


public interface EmailSenderService {

    void sendHtmlMessage(EmailModel emailModel) throws MessagingException;

}
