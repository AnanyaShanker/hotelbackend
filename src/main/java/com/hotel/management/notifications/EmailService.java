package com.hotel.management.notifications;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromAddress = "yourhotel@example.com"; // change to real

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public boolean sendHtmlEmailWithAttachment(String to,
            String subject,
            String htmlBody,
            byte[] attachmentBytes,
            String attachmentName) {
try {
MimeMessage message = mailSender.createMimeMessage();
MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

helper.setFrom(fromAddress);
helper.setTo(to);
helper.setSubject(subject);
helper.setText(htmlBody, true);

// Add attachment (assuming PDF, change MIME type if needed)
helper.addAttachment(attachmentName, new ByteArrayDataSource(attachmentBytes, "application/pdf"));

mailSender.send(message);
return true;

} catch (Exception ex) {
ex.printStackTrace();
return false;
}
}


    public boolean sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML
            mailSender.send(message);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}

