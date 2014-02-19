package org.myweb.services.user.infos;

import org.jetbrains.annotations.NotNull;
import org.myweb.services.RestServiceResult;
import org.myweb.services.ServiceException;

public interface UserInfosServiceRest {
    @NotNull
    public RestServiceResult getUserInfos() throws ServiceException;
}
