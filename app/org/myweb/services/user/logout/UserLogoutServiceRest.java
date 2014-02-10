package org.myweb.services.user.logout;

import org.jetbrains.annotations.NotNull;
import org.myweb.services.RestServiceResult;

public interface UserLogoutServiceRest {
    @NotNull
    public RestServiceResult logoutUser();
}
