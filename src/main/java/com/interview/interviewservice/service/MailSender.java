package com.interview.interviewservice.service;

import com.google.gson.Gson;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Component
@NoArgsConstructor
public class MailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${interview.mail.username}")
    private String sender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${base.url}")
    private String baseUrl;

    private final Logger logger = LoggerFactory.getLogger(MailSender.class);

    @Async
    public void send(String to, String subject, HashMap<String, String> data, String template) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        Context context = new Context();

        data.forEach((key , value) -> {
            context.setVariable(key, value);
        });
        context.setVariable("url", baseUrl);

        helper.setFrom(sender);
        helper.setTo(to);
        helper.setSubject(subject);
        String html = templateEngine.process(template, context);

        helper.setText(html, true);

        javaMailSender.send(mimeMessage);
    }
}
