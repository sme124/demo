package com.example.ssdemoapp.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
public class MailService {

  private static final String BASE_URL = "baseUrl";

  @Value("${demo.mail.baseUrl}")
  private String baseUrl;

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  private MessageSource messageSource;

  @Autowired
  private SpringTemplateEngine templateEngine;

  @Async
  public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
    log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
        isMultipart, isHtml, to, subject, content);
    // Prepare message using a Spring helper
    final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
      message.setTo(to);
      mimeMessage.setFrom("no-reply@test.com");
      message.setSubject(subject);
      message.setText(content, isHtml);
      javaMailSender.send(mimeMessage);
      log.info("Sent email to User '{}' and from '{}'", to, "no-reply@test.com");
    } catch (final Exception e) {
      if (log.isDebugEnabled()) {
        log.warn("Email could not be sent to user '{}'", to, e);
      } else {
        log.warn("Email could not be sent to user '{}': {}", to, e.getMessage() , e);
      }
    }
  }

  @Async
  public void sendEmailFromTemplate(Map<String, Object> userDetails, String templateName, String titleKey, String appendText) {
    final Locale locale = Locale.forLanguageTag("en");
    final Context context = new Context(locale);
    try {
      if (userDetails != null) {
        context.setVariables(userDetails);
        context.setVariable(BASE_URL, baseUrl + appendText);
        final String content = templateEngine.process(templateName, context);
        final String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(String.valueOf(userDetails.get("email")), subject, content, false, true);
      }
    } catch (Exception e) {
      log.warn("Email could not be sent to user '{}':",  e.getMessage());
    }
  }

  @Async
  public void sendVerificationEmail(Map<String, Object> userDetails) {
    log.info("Sending verification email to '{}'", userDetails.get("email"));
    sendEmailFromTemplate(userDetails, "registrationVerification", "account.activation.title", "users/" + userDetails.get("email") + "/email-verification?code=" + userDetails.get("activationCode"));
  }
}
