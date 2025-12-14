package com.isagulova.spring_eshop.service;

import com.isagulova.spring_eshop.domain.User;

public interface MailSenderService {
    void sendActivateCode(User user);
}
