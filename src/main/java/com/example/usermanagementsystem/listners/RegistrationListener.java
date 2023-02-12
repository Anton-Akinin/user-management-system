package com.example.usermanagementsystem.listners;

import com.example.usermanagementsystem.events.OnCreateUserEvent;
import com.example.usermanagementsystem.model.entity.User;
import com.example.usermanagementsystem.service.MailSenderServiceImpl;
import com.example.usermanagementsystem.service.interfaces.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnCreateUserEvent> {
    private final MailSenderService mailSenderService;


    @Autowired
    public RegistrationListener(
            MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @Override
    public void onApplicationEvent(OnCreateUserEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnCreateUserEvent event) {
        User user = event.getUser();

        String emailTo = user.getEmail();
        String subject = "An account has been created for you";
        String text = "Credentials:\n" +
                "Email: " + emailTo + "\n" +
                "A temporary password: " + user.getPassword();

        mailSenderService.sendMessage(emailTo, subject, text);
    }
}