package org.myweb.services.user.infos;

import com.google.inject.Inject;
import models.user.User;
import org.jetbrains.annotations.NotNull;
import org.myweb.services.RestServiceResult;
import org.myweb.services.ServiceException;
import org.myweb.services.crud.get.GetServiceRest;
import org.myweb.utils.session.SessionUtilsService;
import play.i18n.Messages;

import static play.mvc.Http.Status.UNAUTHORIZED;

public class UserInfosServiceRestImpl implements UserInfosServiceRest {

    private final SessionUtilsService sessionUtilsService;
    private final GetServiceRest getServiceRest;

    @Inject
    public UserInfosServiceRestImpl(SessionUtilsService sessionUtilsService, GetServiceRest getServiceRest) {
        this.sessionUtilsService = sessionUtilsService;
        this.getServiceRest = getServiceRest;
    }

    @NotNull
    @Override
    public RestServiceResult getUserInfos() throws ServiceException {

        Long userId = sessionUtilsService.getSessionUserId();
        if(userId == null) {
            throw new ServiceException(
                    UserInfosServiceRestImpl.class.getName(), UNAUTHORIZED,
                    "trying to get session user id but no userId found in cookie", Messages.get("global.unauthorized")
            );
        }

        return getServiceRest.get(User.class, userId);
    }
}
