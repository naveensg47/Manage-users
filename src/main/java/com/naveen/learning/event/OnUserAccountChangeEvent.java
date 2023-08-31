package com.naveen.learning.event;

import com.naveen.learning.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnUserAccountChangeEvent extends ApplicationEvent {

    private User user;
    private String action;
    private String status;

    public OnUserAccountChangeEvent(User user, String action, String status) {
        super(user);
        this.user = user;
        this.action = action;
        this.status = status;
    }
}
