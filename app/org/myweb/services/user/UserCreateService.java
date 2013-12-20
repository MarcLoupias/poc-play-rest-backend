package org.myweb.services.user;

import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.Dao;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.Service;
import org.myweb.services.crud.CreateService;
import org.myweb.utils.ExceptionUtils;
import org.myweb.utils.SecurityUtils;
import play.data.Form;
import play.i18n.Messages;
import play.libs.Json;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.CREATED;
import static play.mvc.Http.Status.INTERNAL_SERVER_ERROR;

public class UserCreateService extends Service {
    private static UserCreateService instance = new UserCreateService();

    public static UserCreateService getInstance(@NotNull Dao db) {
        instance.setDb(db);
        return instance;
    }

    private UserCreateService() {

    }

    @Nullable
    private RestServiceResult checkLogin(@NotNull String login) {
        Map<String, Object> param = new HashMap<>();
        param.put("login", login);
        User res = (User) instance.getDb().namedQuerySingleResult("User.findByLogin", User.class, param);
        if(res != null) {
            return RestServiceResult.buildServiceResult(
                    BAD_REQUEST,
                    "login " + login + " already exist",
                    Messages.get("user.create.error.login.exist", login)
            );
        }

        return null;
    }

    @Nullable
    private RestServiceResult checkEmail(@NotNull String email) {
        Map<String, Object> param = new HashMap<>();
        param.put("email", email);
        User res = (User) instance.getDb().namedQuerySingleResult("User.findByEmail", User.class, param);
        if(res != null) {
            return RestServiceResult.buildServiceResult(
                    BAD_REQUEST,
                    "email " + email + " already exist",
                    Messages.get("user.create.error.email.exist", email)
            );
        }

        return null;
    }

    @NotNull
    public RestServiceResult createUser(@NotNull JsonNode jsContent) {

        Form<User> userForm = Form.form(User.class);
        userForm = userForm.bind(jsContent);

        if(userForm.hasErrors()) {

            return RestServiceResult.buildServiceResult(BAD_REQUEST, userForm.errorsAsJson());

        } else {
            User newUser = userForm.bind(jsContent).get();

            RestServiceResult checkResult;

            checkResult = this.checkLogin(newUser.getLogin());
            if(checkResult != null) {
                return checkResult;
            }

            checkResult = this.checkEmail(newUser.getEmail());
            if(checkResult != null) {
                return checkResult;
            }

            if( newUser.getNewPassword() == null || newUser.getConfirmPassword() == null ||
                    (!newUser.getNewPassword().equals(newUser.getConfirmPassword())) ){
                return RestServiceResult.buildServiceResult(
                        BAD_REQUEST,
                        "input passwords doesn't match",
                        Messages.get("user.create.error.pwd.not.match")
                );
            }

            newUser.getUserPasswordSettings().setDefaultConfig();

            newUser.getUserPasswordSettings().setSalt(
                    SecurityUtils.generateSalt(
                            newUser.getUserPasswordSettings().getSaltByteLength()
                    )
            );

            try {
                newUser.getUserPasswordSettings().setHashedPwd(
                        SecurityUtils.hash(
                                newUser.getNewPassword().toCharArray(),
                                newUser.getUserPasswordSettings().getSalt(),
                                newUser.getUserPasswordSettings().getIterations(),
                                newUser.getUserPasswordSettings().getPwdDerivatedKeyBitLength(),
                                newUser.getUserPasswordSettings().getPwdPBKDF2algo()
                        )
                );
            } catch (NoSuchAlgorithmException e) {
                return RestServiceResult.buildServiceResult(
                        INTERNAL_SERVER_ERROR,
                        "Given hash algorithm does not exist or not supported ! (" +
                                newUser.getUserPasswordSettings().getPwdPBKDF2algo() + ")",
                        Messages.get("user.create.error.internal.server.error")
                );
            } catch (InvalidKeySpecException e) {
                return RestServiceResult.buildServiceResult(
                        INTERNAL_SERVER_ERROR,
                        ExceptionUtils.throwableToString(e),
                        Messages.get("user.create.error.internal.server.error")
                );
            }

            newUser.setNewPassword(null);
            newUser.setConfirmPassword(null);

            JavaServiceResult jsr = CreateService.getInstance(instance.getDb()).saveNew(newUser);

            return RestServiceResult.buildServiceResult(CREATED, Json.toJson(jsr.getSingleContent()));
        }

    }
}
