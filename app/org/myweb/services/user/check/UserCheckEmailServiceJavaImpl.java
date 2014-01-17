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

public class UserCheckEmailServiceJavaImpl implements UserCheckEmailServiceJava {

    private final Dao dao;

    @Inject
    public UserCheckEmailServiceJavaImpl(Dao dao) {
        this.dao = dao;
    }

    @NotNull
    @Override
    public JavaServiceResult check(@NotNull String email) {
        Map<String, Object> param = new HashMap<>();
        param.put("email", email);
        User res = (User) dao.namedQuerySingleResult("User.findByEmail", User.class, param);
        if(res != null) {
            return JavaServiceResult.buildServiceResult(
                    BAD_REQUEST,
                    "email " + email + " already exist",
                    Messages.get("user.check.email.error.email.exist", email)
            );
        }

        return JavaServiceResult.buildServiceResult(OK);
    }

    @NotNull
    @Override
    public JavaServiceResult checkExcludingUserId(@NotNull String email, @NotNull Long excludedUserId) {
        Map<String, Object> param = new HashMap<>();
        param.put("email", email);
        param.put("excludedId", excludedUserId);
        User res = (User) dao.namedQuerySingleResult("User.findByEmailExcludingId", User.class, param);
        if(res != null) {
            return JavaServiceResult.buildServiceResult(
                    BAD_REQUEST,
                    "email " + email + " already exist",
                    Messages.get("user.check.email.error.email.exist", email)
            );
        }

        return JavaServiceResult.buildServiceResult(OK);
    }
}
