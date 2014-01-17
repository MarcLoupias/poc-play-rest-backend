package org.myweb.utils.mail;

import org.jetbrains.annotations.NotNull;

public interface MailUtilsService {
    public void sendTechTextEmail(@NotNull String recipient, @NotNull String subject, @NotNull String textContent);

    public void sendTextEmail(
            @NotNull String sender, @NotNull String recipient, @NotNull String subject, @NotNull String textContent
    );
}
