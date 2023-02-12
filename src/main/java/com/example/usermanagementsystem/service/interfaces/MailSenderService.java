package com.example.usermanagementsystem.service.interfaces;

public interface MailSenderService {
    void sendMessage(String sendTo, String subject, String text);
}
