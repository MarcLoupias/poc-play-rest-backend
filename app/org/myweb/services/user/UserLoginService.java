package org.myweb.services.user;

import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import models.UserPasswordSettings;
import org.jetbrains.annotations.NotNull;
import org.myweb.db.Dao;
import org.myweb.services.RestServiceResult;
import org.myweb.services.Service;
import org.myweb.utils.ExceptionUtils;
import org.myweb.utils.SecurityUtils;
import org.myweb.utils.SessionUtils;
import play.data.Form;
import play.i18n.Messages;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import static play.mvc.Controller.session;
import static play.mvc.Http.Status.*;

public class UserLoginService extends Service {
    private static UserLoginService instance = new UserLoginService();

    public static UserLoginService getInstance(@NotNull Dao db) {
        instance.setDb(db);
        return instance;
    }

    private UserLoginService() {

    }

    @NotNull
    public RestServiceResult loginUser(@NotNull JsonNode jsContent) {
        Form<UserLoginAttempt> userLoginAttemptForm = Form.form(UserLoginAttempt.class);
        userLoginAttemptForm = userLoginAttemptForm.bind(jsContent);

        if(userLoginAttemptForm.hasErrors()) {

            return RestServiceResult.buildServiceResult(BAD_REQUEST, userLoginAttemptForm.errorsAsJson());

        } else {
            UserLoginAttempt userLoginAttempt = userLoginAttemptForm.bind(jsContent).get();

            User user;
            Map<String, Object> param;

            if(userLoginAttempt.getLogin() != null && !userLoginAttempt.getLogin().isEmpty()) {
                param = new HashMap<>();
                param.put("login", userLoginAttempt.getLogin());
                user = (User) instance.getDb().namedQuerySingleResult("User.findByLogin", User.class, param);
            } else if(userLoginAttempt.getEmail() != null && !userLoginAttempt.getEmail().isEmpty()) {
                param = new HashMap<>();
                param.put("email", userLoginAttempt.getEmail());
                user = (User) instance.getDb().namedQuerySingleResult("User.findByEmail", User.class, param);
            } else {
                return RestServiceResult.buildServiceResult(
                        BAD_REQUEST,
                        "Login request, login & email missing",
                        Messages.get("login.attempt.validation.error.loginAndEmail")
                );
            }

            if(user == null) {
                return RestServiceResult.buildServiceResult(
                        NOT_FOUND,
                        "user id (login=" + userLoginAttempt.getLogin() +
                                " - email=" + userLoginAttempt.getEmail() + ") not found",
                        Messages.get("login.attempt.user.not.found")
                );
            }

            UserPasswordSettings pwdSettings = user.getUserPasswordSettings();

            boolean loginResult;
            try {

                loginResult = SecurityUtils.isExpectedPassword(
                        userLoginAttempt.getPassword().toCharArray(),
                        pwdSettings.getSalt(),
                        pwdSettings.getHashedPwd(),
                        pwdSettings.getIterations(),
                        pwdSettings.getPwdDerivatedKeyBitLength(),
                        pwdSettings.getPwdPBKDF2algo()
                );

            } catch (InvalidKeySpecException e) {
                return RestServiceResult.buildServiceResult(
                        INTERNAL_SERVER_ERROR,
                        ExceptionUtils.throwableToString(e),
                        Messages.get("login.attempt.error.internal.server.error")
                );
            } catch (NoSuchAlgorithmException e) {
                return RestServiceResult.buildServiceResult(
                        INTERNAL_SERVER_ERROR,
                        "Given hash algorithm does not exist or not supported ! (" +
                                pwdSettings.getPwdPBKDF2algo() + ")",
                        Messages.get("login.attempt.error.internal.server.error")
                );
            }

            if(!loginResult) {
                return RestServiceResult.buildServiceResult(
                        UNAUTHORIZED,
                        "Failed login attempt for user " + user.getLogin() + ", bad password.",
                        Messages.get("login.attempt.error.bad.pwd")
                );
            }

            session(SessionUtils.SESSION_USER_ID_KEY, String.valueOf(user.getId()));

            return RestServiceResult.buildServiceResult(OK);
        }
    }
}
