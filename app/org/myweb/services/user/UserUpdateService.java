package org.myweb.services.user;

import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.Dao;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.Service;
import org.myweb.services.crud.GetService;
import org.myweb.services.crud.UpdateService;
import org.myweb.utils.ExceptionUtils;
import org.myweb.utils.SecurityUtils;
import play.data.Form;
import play.i18n.Messages;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import static play.mvc.Http.Status.*;
import static play.mvc.Http.Status.NOT_FOUND;

public class UserUpdateService extends Service {
    private static UserUpdateService instance = new UserUpdateService();

    public static UserUpdateService getInstance(@NotNull Dao db) {
        instance.setDb(db);
        return instance;
    }

    private UserUpdateService() {

    }

    @Nullable
    private RestServiceResult checkLogin(@NotNull String login, @NotNull Long id) {
        Map<String, Object> param = new HashMap<>();
        param.put("login", login);
        param.put("excludedId", id);
        User res = (User) instance.getDb().namedQuerySingleResult("User.findByLoginExcludingId", User.class, param);
        if(res != null) {
            return RestServiceResult.buildServiceResult(
                    BAD_REQUEST,
                    "login " + login + " already exist",
                    Messages.get("user.update.error.login.exist", login)
            );
        }

        return null;
    }

    @Nullable
    private RestServiceResult checkEmail(@NotNull String email, @NotNull Long id) {
        Map<String, Object> param = new HashMap<>();
        param.put("email", email);
        param.put("excludedId", id);
        User res = (User) instance.getDb().namedQuerySingleResult("User.findByEmailExcludingId", User.class, param);
        if(res != null) {
            return RestServiceResult.buildServiceResult(
                    BAD_REQUEST,
                    "email " + email + " already exist",
                    Messages.get("user.update.error.email.exist", email)
            );
        }

        return null;
    }

    @NotNull
    public RestServiceResult updateUser(@NotNull JsonNode jsContent, @Nullable Long userId) {

        if(userId == null || userId < 1l) {
            return RestServiceResult.buildServiceResult(
                    BAD_REQUEST,
                    "put request without user id",
                    Messages.get("user.update.error.id.missing")
            );
        }

        Form<User> userForm = Form.form(User.class);
        userForm = userForm.bind(jsContent);

        if(userForm.hasErrors()) {

            return RestServiceResult.buildServiceResult(BAD_REQUEST, userForm.errorsAsJson());

        } else {
            User user = userForm.bind(jsContent).get();

            if(user.getId() == null || (user.getId().longValue() != userId.longValue()) ){
                return RestServiceResult.buildServiceResult(
                        BAD_REQUEST,
                        "put request id (" + userId + ") differ from put content id (" + user.getId() + ")",
                        Messages.get("user.update.error.url.id.and.pojo.id.differ")
                );
            }

            RestServiceResult checkResult;

            checkResult = this.checkLogin(user.getLogin(), user.getId());
            if(checkResult != null) {
                return checkResult;
            }

            checkResult = this.checkEmail(user.getEmail(), user.getId());
            if(checkResult != null) {
                return checkResult;
            }

            if( user.getNewPassword() == null || user.getConfirmPassword() == null ||
                    (!user.getNewPassword().equals(user.getConfirmPassword())) ){
                return RestServiceResult.buildServiceResult(
                        BAD_REQUEST,
                        "input passwords doesn't match",
                        Messages.get("user.update.error.pwd.not.match")
                );
            }

            user.getUserPasswordSettings().setDefaultConfig();

            user.getUserPasswordSettings().setSalt(
                    SecurityUtils.generateSalt(
                            user.getUserPasswordSettings().getSaltByteLength()
                    )
            );

            try {
                user.getUserPasswordSettings().setHashedPwd(
                        SecurityUtils.hash(
                                user.getNewPassword().toCharArray(),
                                user.getUserPasswordSettings().getSalt(),
                                user.getUserPasswordSettings().getIterations(),
                                user.getUserPasswordSettings().getPwdDerivatedKeyBitLength(),
                                user.getUserPasswordSettings().getPwdPBKDF2algo()
                        )
                );
            } catch (NoSuchAlgorithmException e) {
                return RestServiceResult.buildServiceResult(
                        INTERNAL_SERVER_ERROR,
                        "Given hash algorithm does not exist or not supported ! (" +
                                user.getUserPasswordSettings().getPwdPBKDF2algo() + ")",
                        Messages.get("user.update.error.internal.server.error")
                );
            } catch (InvalidKeySpecException e) {
                return RestServiceResult.buildServiceResult(
                        INTERNAL_SERVER_ERROR,
                        ExceptionUtils.throwableToString(e),
                        Messages.get("user.update.error.internal.server.error")
                );
            }

            RestServiceResult rsrLoad = GetService.getInstance(instance.getDb()).get(User.class, userId);
            if(rsrLoad.getHttpStatus() == NOT_FOUND) {
                return RestServiceResult.buildServiceResult(NOT_FOUND, rsrLoad.getErrorMsg(), rsrLoad.getUserMsg());
            }

            JavaServiceResult jsr = UpdateService.getInstance(instance.getDb()).merge(user);

            return RestServiceResult.buildServiceResult(jsr.getHttpStatus());
        }
    }
}
