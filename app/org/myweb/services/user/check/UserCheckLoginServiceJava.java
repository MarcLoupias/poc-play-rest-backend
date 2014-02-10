package org.myweb.services.user.check;

import org.jetbrains.annotations.NotNull;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.ServiceException;

public interface UserCheckLoginServiceJava {
    @NotNull
    public JavaServiceResult check(@NotNull String login) throws ServiceException;

    @NotNull
    public JavaServiceResult checkExcludingUserId(@NotNull String login, @NotNull Long excludedUserId) throws ServiceException;
}
