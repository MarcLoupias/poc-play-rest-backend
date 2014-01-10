package org.myweb.utils;

import com.sun.mail.smtp.SMTPTransport;
import org.jetbrains.annotations.NotNull;
import play.Logger;
import play.Play;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtils {

    public static void sendTechTextEmail(@NotNull String recipient, @NotNull String subject, @NotNull String textContent) {
        MailUtils.sendTextEmail("noreply@poc-play-rest-backed.org", recipient, subject, textContent);
    }

    public static void sendTextEmail(
            @NotNull String sender, @NotNull String recipient, @NotNull String subject, @NotNull String textContent
    ) {

        if(Play.isTest()){
            return;
        }

        String smtpHostName, smtpAuthUser, smtpAuthPwd;
        smtpHostName = "smtp.sendgrid.net";

        try {

            smtpAuthUser = System.getenv("SENDGRID_USERNAME");
            smtpAuthPwd = System.getenv("SENDGRID_PASSWORD");

            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", smtpHostName);
            props.put("mail.smtp.port", 587);
            props.put("mail.smtp.auth", "true");

            Session mailSession = Session.getInstance(props, null);
            // uncomment for debugging infos to stdout
            // mailSession.setDebug(true);
            SMTPTransport transport = (SMTPTransport)mailSession.getTransport();

            MimeMessage message = new MimeMessage(mailSession);

            message.setFrom(new InternetAddress(sender));
            message.setSubject(subject);
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(recipient));

            message.setText(textContent);

            transport.connect(smtpHostName, smtpAuthUser, smtpAuthPwd);
            transport.sendMessage(message,
                    message.getRecipients(Message.RecipientType.TO));
            transport.close();

        } catch (Exception e) {
            Logger.error(ExceptionUtils.throwableToString(e));
        }
    }

}
