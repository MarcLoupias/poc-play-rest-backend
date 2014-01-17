package org.myweb.services.user.create;

import models.user.User;
import org.jetbrains.annotations.NotNull;
import org.myweb.services.JavaServiceResult;

public interface UserCreateServiceJava {
    @NotNull
    public JavaServiceResult createUser(@NotNull User user);
}
