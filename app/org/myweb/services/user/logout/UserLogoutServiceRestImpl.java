package org.myweb.services.user.logout;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.myweb.services.RestServiceResult;
import org.myweb.utils.session.SessionUtilsService;

import static play.mvc.Http.Status.*;

public class UserLogoutServiceRestImpl implements UserLogoutServiceRest {

    private final SessionUtilsService sessionUtilsService;

    @Inject
    public UserLogoutServiceRestImpl(SessionUtilsService sessionUtilsService) {
        this.sessionUtilsService = sessionUtilsService;
    }

    @NotNull
    @Override
    public RestServiceResult logoutUser() {

        sessionUtilsService.deleteSessionUserId();

        return RestServiceResult.buildServiceResult(OK);
    }
}
