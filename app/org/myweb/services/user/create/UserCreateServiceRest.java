package org.myweb.services.user.create;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.myweb.services.RestServiceResult;
import org.myweb.services.ServiceException;

public interface UserCreateServiceRest {
    @NotNull
    public RestServiceResult createUser(@NotNull JsonNode jsContent) throws ServiceException;
}
