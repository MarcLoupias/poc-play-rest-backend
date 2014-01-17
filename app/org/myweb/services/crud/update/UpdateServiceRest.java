package org.myweb.services.crud.update;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.DaoObject;
import org.myweb.services.RestServiceResult;

public interface UpdateServiceRest {
    @NotNull
    public RestServiceResult update(
            @NotNull Class<? extends DaoObject> clazz, @NotNull JsonNode jsContent, @Nullable Long entityId
    );
}
