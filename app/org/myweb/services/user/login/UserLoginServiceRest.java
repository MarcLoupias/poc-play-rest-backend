package org.myweb.services.user.login;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.myweb.services.RestServiceResult;

public interface UserLoginServiceRest {
    @NotNull
    public RestServiceResult loginUser(@NotNull JsonNode jsContent);
}
