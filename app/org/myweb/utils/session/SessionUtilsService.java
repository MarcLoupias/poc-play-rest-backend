package org.myweb.utils.session;

import org.jetbrains.annotations.Nullable;

public interface SessionUtilsService {
    @Nullable
    public Long getSessionUserId();

    public void setSessionUserId(Long id);
}
