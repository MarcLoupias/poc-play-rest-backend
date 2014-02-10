package org.myweb.services.user.create;

import models.user.User;
import org.jetbrains.annotations.NotNull;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.ServiceException;

public interface UserCreateServiceJava {
    @NotNull
    public JavaServiceResult createUser(@NotNull User user) throws ServiceException;
}
