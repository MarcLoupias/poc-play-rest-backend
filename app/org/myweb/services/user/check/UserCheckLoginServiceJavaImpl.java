package org.myweb.services.user.check;

import com.google.inject.Inject;
import models.user.User;
import org.jetbrains.annotations.NotNull;
import org.myweb.db.Dao;
import org.myweb.services.JavaServiceResult;
import play.i18n.Messages;

import java.util.HashMap;
import java.util.Map;

import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;

public class UserCheckLoginServiceJavaImpl implements UserCheckLoginServiceJava {

    private final Dao dao;

    @Inject
    public UserCheckLoginServiceJavaImpl(Dao dao) {
        this.dao = dao;
    }

    @NotNull
    @Override
    public JavaServiceResult check(@NotNull String login) {
        Map<String, Object> param = new HashMap<>();
        param.put("login", login);
        User res = (User) dao.namedQuerySingleResult("User.findByLogin", User.class, param);
        if(res != null) {
            return JavaServiceResult.buildServiceResult(
                    BAD_REQUEST,
                    "login " + login + " already exist",
                    Messages.get("user.check.login.error.login.exist", login)
            );
        }

        return JavaServiceResult.buildServiceResult(OK);
    }

    @NotNull
    @Override
    public JavaServiceResult checkExcludingUserId(@NotNull String login, @NotNull Long excludedUserId) {
        Map<String, Object> param = new HashMap<>();
        param.put("login", login);
        param.put("excludedId", excludedUserId);
        User res = (User) dao.namedQuerySingleResult("User.findByLoginExcludingId", User.class, param);
        if(res != null) {
            return JavaServiceResult.buildServiceResult(
                    BAD_REQUEST,
                    "login " + login + " already exist",
                    Messages.get("user.check.login.error.login.exist", login)
            );
        }

        return JavaServiceResult.buildServiceResult(OK);
    }
}
