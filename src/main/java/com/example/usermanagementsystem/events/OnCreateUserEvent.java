package com.example.usermanagementsystem.events;


import com.example.usermanagementsystem.model.entity.User;
import org.springframework.context.ApplicationEvent;

public class OnCreateUserEvent extends ApplicationEvent {
    private User user;

    public OnCreateUserEvent(User user) {
        super(user);

        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public OnCreateUserEvent setUser(User user) {
        this.user = user;
        return this;
    }
}
