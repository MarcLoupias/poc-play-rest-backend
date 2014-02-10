package org.myweb.services.user.check;

import org.jetbrains.annotations.NotNull;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.ServiceException;

public interface UserCheckEmailServiceJava {
    @NotNull
    public JavaServiceResult check(@NotNull String email) throws ServiceException;

    @NotNull
    public JavaServiceResult checkExcludingUserId(@NotNull String email, @NotNull Long excludedUserId) throws ServiceException;
}
