package com.naveen.learning.event.listener;

import com.naveen.learning.event.OnUserAccountChangeEvent;
import com.naveen.learning.model.User;
import com.naveen.learning.services.MailService;
import com.naveen.learning.utils.error.ErrorCodeHelper;
import com.naveen.learning.utils.error.response.ErrorInfo;
import com.naveen.learning.utils.error.response.ServiceException;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import freemarker.template.TemplateException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class OnUserAccountChangeEventListener implements ApplicationListener<OnUserAccountChangeEvent> {

    @Autowired
    private MailService mailService;

    @Autowired
    private ErrorCodeHelper errorCodeHelper;

    @Override
    public void onApplicationEvent(OnUserAccountChangeEvent event) {
        sendAccountChangesEmail(event);
    }

    private void sendAccountChangesEmail(OnUserAccountChangeEvent event) {
        User user = event.getUser();
        String action = event.getAction();
        String status = event.getStatus();
        String toEmail = user.getEmail();

        try {
            mailService.sendAccountChangesEmail(action, status, toEmail);
        } catch (IOException e) {
            ErrorInfo errorInfo = errorCodeHelper.getErrorInfo("E2003", "Account Change Email Failed to Send");
            throw new ServiceException(errorInfo);
        }
    }
}
