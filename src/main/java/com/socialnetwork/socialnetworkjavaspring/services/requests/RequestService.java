package com.socialnetwork.socialnetworkjavaspring.services.requests;

import com.socialnetwork.socialnetworkjavaspring.models.Request;
import com.socialnetwork.socialnetworkjavaspring.repositories.IRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.Optional;

@Service
@Transactional
public class RequestService implements IRequestService {
    @Autowired
    private IRequestRepository requestRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public Optional<Request> save(Request object) {
        object.setRequestCode(generateCode());
        requestRepository.save(object);
        return Optional.of(object);
    }

    @Override
    public Optional<Request> delete(Request object) {
        try {
            requestRepository.delete(object);
            return Optional.of(object);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String generateCode() {
        int codeRandom = (int) Math.floor(((Math.random() * 899999) + 100000));
        return Integer.toString(codeRandom);
    }


    @Override
    public Optional<Request> findByEmailRequest(String email) {
        return requestRepository.findByEmailRequest(email);
    }

    @Override
    public Boolean sendCodeToEmail(String toEmail, String subject, String code) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String htmlContent = "<!DOCTYPE html>" +
                    "<html lang=\"en\">" +
                    "<head>" +
                    "<meta charset=\"UTF-8\">" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f6f6f6; margin: 0; padding: 0; }" +
                    ".container { width: 100%; padding: 20px; background-color: #f6f6f6; }" +
                    ".content { max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                    ".header { text-align: center; padding: 10px 0; }" +
                    ".header img { max-width: 150px; }" +
                    ".header h1 { margin: 0; font-size: 24px; color: #333333; }" +
                    ".body { padding: 20px; color: #666666; line-height: 1.6; text-align: center; }" +
                    ".body h2 { color: #333333; font-size: 20px; }" +
                    ".body p { margin: 10px 0; }" +
                    ".footer { text-align: center; padding: 20px; font-size: 12px; color: #999999; }" +
                    ".button { display: inline-block; padding: 10px 20px; margin: 20px 0; background-color: #007bff; color: #ffffff; text-decoration: none; border-radius: 5px; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class=\"container\">" +
                    "<div class=\"content\">" +
                    "<div class=\"header\">" +
                    "<h1>Verification Code</h1>" +
                    "</div>" +
                    "<div class=\"body\">" +
                    "<h2>Hello,</h2>" +
                    "<p>Your verification code is:</p>" +
                    "<p><strong>" + code + "</strong></p>" +
                    "<p>Please use this code to complete your verification.</p>" +
                    "</div>" +
                    "<div class=\"footer\">" +
                    "<p>&copy; 2024 Your Company. All rights reserved.</p>" +
                    "</div>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);  // true indicates the message is HTML

            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean clearRequestCodeExpired() {
        try {
            requestRepository.deleteAllByExpiredRequestCode();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
