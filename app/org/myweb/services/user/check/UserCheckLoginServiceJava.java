package org.myweb.services.user.check;

import org.jetbrains.annotations.NotNull;
import org.myweb.services.JavaServiceResult;

public interface UserCheckLoginServiceJava {
    @NotNull
    public JavaServiceResult check(@NotNull String login);

    @NotNull
    public JavaServiceResult checkExcludingUserId(@NotNull String login, @NotNull Long excludedUserId);
}
