package org.myweb.utils.mail;

import com.google.inject.Inject;
import com.sun.mail.smtp.SMTPTransport;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.myweb.utils.config.EnvConfigService;
import org.myweb.utils.config.EnvConfigServiceException;
import play.Logger;
import play.Play;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtilsServiceJavamailImpl implements MailUtilsService {

    private final EnvConfigService envConfigService;

    @Inject
    public MailUtilsServiceJavamailImpl(EnvConfigService envConfigService) {
        this.envConfigService = envConfigService;
    }

    public void sendTechTextEmail(@NotNull String recipient, @NotNull String subject, @NotNull String textContent) {
        String sender;
        try {
            sender = envConfigService.getEnvVarAsString(EnvConfigService.PPRB_NOREPLY_EMAIL);
        } catch (EnvConfigServiceException e) {
            Logger.error("Exception when trying to get PPRB_NOREPLY_EMAIL env var :\n" + ExceptionUtils.getStackTrace(e));
            return;
        }
        if(sender == null) {
            sender = "noreply@poc-play-rest-backed.org";
        }
        sendTextEmail(sender, recipient, subject, textContent);
    }

    public void sendTextEmail(
            @NotNull String sender, @NotNull String recipient, @NotNull String subject, @NotNull String textContent
    ) {

        if(Play.isTest() || Play.isDev()){
            return;
        }

        String smtpHostName, smtpAuthUser, smtpAuthPwd;


        try {
            smtpHostName = envConfigService.getEnvVarAsString(EnvConfigService.PPRB_MAILER_SMTP_URL);
            smtpAuthUser = envConfigService.getEnvVarAsString(EnvConfigService.PPRB_MAILER_SMTP_USERNAME);
            smtpAuthPwd = envConfigService.getEnvVarAsString(EnvConfigService.PPRB_MAILER_SMTP_PWD);

            if(smtpHostName == null) {
                Logger.warn("Mailer url is not set. Impossible to send an email.");
                return;
            }
            if(smtpAuthUser == null && smtpAuthPwd == null) {
                Logger.warn("Mailer username and pwd are not set. Impossible to send an email.");
                return;
            }
            if(smtpAuthUser == null) {
                Logger.warn("Mailer username is not set. Impossible to send an email.");
                return;
            }
            if(smtpAuthPwd == null) {
                Logger.warn("Mailer password is not set. Impossible to send an email.");
                return;
            }

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
            Logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

}
