package com.naveen.learning.event;

import com.naveen.learning.model.PasswordResetToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
public class OnPasswordResetLinkEvent extends ApplicationEvent {

    private transient UriComponentsBuilder redirectURI;
    private transient PasswordResetToken passwordResetToken;

    public OnPasswordResetLinkEvent(PasswordResetToken passwordResetToken, UriComponentsBuilder redirectUrl) {
        super(passwordResetToken);
        this.passwordResetToken = passwordResetToken;
        this.redirectURI = redirectUrl;
    }
}
