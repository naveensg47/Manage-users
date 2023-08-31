package com.naveen.learning.event.listener;

import com.naveen.learning.event.OnPasswordResetLinkEvent;
import com.naveen.learning.model.PasswordResetToken;
import com.naveen.learning.model.User;
import com.naveen.learning.services.MailService;
import com.naveen.learning.utils.error.ErrorCodeHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OnPasswordResetLinkEventListener implements ApplicationListener<OnPasswordResetLinkEvent> {
    private static final Logger logger = Logger.getLogger(OnPasswordResetLinkEventListener.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private ErrorCodeHelper errorCodeHelper;

    @Override
    @Async
    public void onApplicationEvent(OnPasswordResetLinkEvent event) {
        sentResetLink(event);
    }

    private void sentResetLink(OnPasswordResetLinkEvent event) {
        PasswordResetToken passwordResetToken = event.getPasswordResetToken();
        User user = passwordResetToken.getUser();
        String toMail = user.getEmail();
        String emailConfirmationUrl = event.getRedirectURI().queryParam("token", passwordResetToken.getToken()).toUriString();
        System.out.println("Reset Event Log");
        mailService.sendResetLink(emailConfirmationUrl, toMail);
    }
}
