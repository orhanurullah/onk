package com.onk.core.sendmail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Async
@Service
@Transactional
public class EmailService {



    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;
    private final ONKFreeMarkerConfigFactoryBean onkFreeMarkerConfigFactoryBean;

    public EmailService(JavaMailSender javaMailSender, ONKFreeMarkerConfigFactoryBean onkFreeMarkerConfigFactoryBean) {
        this.javaMailSender = javaMailSender;
        this.onkFreeMarkerConfigFactoryBean = onkFreeMarkerConfigFactoryBean;
    }

    private void sendMail(String from, Set<String> to, Set<String> cc, Set<String> bcc, String subject, List<AttachmentObject> attachments, String freemarkerTemplateName, Map<String, Object> model) {
        sendMail(MailObject.builder()
                .from(from)
                .to(to)
                .cc(cc)
                .bcc(bcc)
                .subject(subject)
                .attachments(attachments)
                .build(), freemarkerTemplateName, model);
    }


    public void sendMail(MailObject mailObject, String freemarkerTemplateName, Map<String, Object> model) {
        mailObject.setBody(geFreeMarkerTemplateContent(freemarkerTemplateName, model));
        try {
            sendMail(mailObject);
        } catch (MessagingException e) {
            LOGGER.error("mail send error", e);
        }
    }

    private void sendMail(@NotNull @Valid MailObject mailObject) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setFrom(mailObject.getFrom());

        if (CollectionUtils.isNotEmpty(mailObject.getTo()))
            helper.setTo(mailObject.getTo().toArray(new String[0]));
        if (CollectionUtils.isNotEmpty(mailObject.getCc()))
            helper.setCc(mailObject.getCc().toArray(new String[0]));
        if (CollectionUtils.isNotEmpty(mailObject.getBcc()))
            helper.setBcc(mailObject.getBcc().toArray(new String[0]));

        helper.setSubject(mailObject.getSubject());
        helper.setText(mailObject.getBody(), true);

        if (CollectionUtils.isNotEmpty(mailObject.getAttachments())) {

            mailObject.getAttachments().forEach(a -> {
                try {
                    helper.addAttachment(a.getFileName(), new ByteArrayResource(a.getData()), a.getContentType());
                } catch (MessagingException e) {
                    LOGGER.error("Error while getting attachment: {}", e.getMessage(), e);
                }
            });
        }

        javaMailSender.send(mimeMessage);

    }

    public String geFreeMarkerTemplateContent(String templatePath, Map<String, Object> model) {
        StringBuilder content = new StringBuilder();
        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(onkFreeMarkerConfigFactoryBean.getTemplate(templatePath), model));
            return content.toString();
        } catch (Exception e) {
            LOGGER.error("Exception occured while processing fmtemplate: {}", e.getMessage(), e);
        }
        return "";
    }

}
