package org.myweb.services.user.check;

import org.jetbrains.annotations.NotNull;
import org.myweb.services.JavaServiceResult;

public interface UserCheckEmailServiceJava {
    @NotNull
    public JavaServiceResult check(@NotNull String email);

    @NotNull
    public JavaServiceResult checkExcludingUserId(@NotNull String email, @NotNull Long excludedUserId);
}
