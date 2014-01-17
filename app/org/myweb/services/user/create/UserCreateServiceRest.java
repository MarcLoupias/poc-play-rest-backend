package org.myweb.services.user.create;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.myweb.services.RestServiceResult;

public interface UserCreateServiceRest {
    @NotNull
    public RestServiceResult createUser(@NotNull JsonNode jsContent);
}
