package com.socialnetwork.socialnetworkjavaspring.services.requests;

import com.socialnetwork.socialnetworkjavaspring.models.Request;
import com.socialnetwork.socialnetworkjavaspring.repositories.IRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setText(code);
            message.setSubject(subject);
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
