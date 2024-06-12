package com.socialnetwork.socialnetworkjavaspring.services.requests;

import com.socialnetwork.socialnetworkjavaspring.models.Request;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;

import java.util.Optional;

public interface IRequestService extends IGeneralService<Request, Long>{
    Optional<Request> save(Request object);

    Optional<Request> findByEmailRequest(String email);

    Boolean sendCodeToEmail(String toEmail, String subject, String code);

    boolean clearRequestCodeExpired();
}
