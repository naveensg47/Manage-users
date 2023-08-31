package com.naveen.learning.services;

import java.io.IOException;

public interface MailService {

    void sendAccountChangesEmail(String action, String status, String toEmail) throws IOException;

    void sendResetLink(String emailConfirmationUrl, String toMail);
}
