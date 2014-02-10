package org.myweb.services.user.update;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.services.RestServiceResult;
import org.myweb.services.ServiceException;

public interface UserUpdateServiceRest {
    @NotNull
    public RestServiceResult updateUser(@NotNull JsonNode jsContent, @Nullable Long userId) throws ServiceException;
}
