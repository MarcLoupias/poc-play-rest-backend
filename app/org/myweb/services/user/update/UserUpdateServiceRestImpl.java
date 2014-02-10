package org.myweb.services.user.update;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.ModelFactoryHelper;
import models.user.User;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.Dao;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.crud.get.GetServiceRest;
import org.myweb.services.user.check.UserCheckEmailServiceJava;
import org.myweb.services.user.check.UserCheckLoginServiceJava;
import org.myweb.utils.security.PasswordGenerationService;
import play.data.Form;
import play.i18n.Messages;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static play.mvc.Http.Status.*;
import static play.mvc.Http.Status.NOT_FOUND;

public class UserUpdateServiceRestImpl implements UserUpdateServiceRest {
    private final Dao dao;
    private final ModelFactoryHelper modelFactoryHelper;
    private final UserCheckLoginServiceJava checkLogin;
    private final UserCheckEmailServiceJava checkEmail;
    private final PasswordGenerationService passwordGenerationService;
    private final GetServiceRest getServiceRest;

    @Inject
    public UserUpdateServiceRestImpl(
            Dao dao, ModelFactoryHelper modelFactoryHelper,
            UserCheckLoginServiceJava checkLogin, UserCheckEmailServiceJava checkEmail,
            PasswordGenerationService passwordGenerationService, GetServiceRest getServiceRest
    ) {
        this.dao = dao;
        this.modelFactoryHelper = modelFactoryHelper;
        this.checkLogin = checkLogin;
        this.checkEmail = checkEmail;
        this.getServiceRest = getServiceRest;
        this.passwordGenerationService = passwordGenerationService;
    }

    @NotNull
    @Override
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
            User formUser = userForm.bind(jsContent).get();

            if(formUser.getId() == null || (formUser.getId().longValue() != userId.longValue()) ){
                return RestServiceResult.buildServiceResult(
                        BAD_REQUEST,
                        "put request id (" + userId + ") differ from put content id (" + formUser.getId() + ")",
                        Messages.get("user.update.error.url.id.and.pojo.id.differ")
                );
            }

            JavaServiceResult checkResult;

            checkResult = checkLogin.checkExcludingUserId(formUser.getLogin(), formUser.getId());
            if(checkResult.getHttpStatus() == BAD_REQUEST) {
                return RestServiceResult.buildServiceResult(checkResult);
            }

            checkResult = checkEmail.checkExcludingUserId(formUser.getEmail(), formUser.getId());
            if(checkResult.getHttpStatus() == BAD_REQUEST) {
                return RestServiceResult.buildServiceResult(checkResult);
            }

            if( formUser.getNewPassword() == null || formUser.getConfirmPassword() == null ||
                    (!formUser.getNewPassword().equals(formUser.getConfirmPassword())) ){
                return RestServiceResult.buildServiceResult(
                        BAD_REQUEST,
                        "input passwords doesn't match",
                        Messages.get("user.update.error.pwd.not.match")
                );
            }

            User userToUpdate = modelFactoryHelper.userFactory(
                    formUser.getId(), formUser.getLogin(), formUser.getNewPassword(),
                    formUser.getConfirmPassword(), formUser.getEmail()
            );

            try {
                assert userToUpdate.getNewPassword() != null;
                userToUpdate.setUserPasswordSettings(
                        passwordGenerationService.generate(
                                userToUpdate.getUserPasswordSettings(), userToUpdate.getNewPassword().toCharArray()
                        )
                );

            } catch (NoSuchAlgorithmException e) {
                return RestServiceResult.buildServiceResult(
                        INTERNAL_SERVER_ERROR,
                        "Given hash algorithm does not exist or not supported ! (" +
                                userToUpdate.getUserPasswordSettings().getPwdPBKDF2algo() + ")",
                        Messages.get("user.update.error.internal.server.error")
                );
            } catch (InvalidKeySpecException e) {
                return RestServiceResult.buildServiceResult(
                        INTERNAL_SERVER_ERROR,
                        ExceptionUtils.getStackTrace(e),
                        Messages.get("user.update.error.internal.server.error")
                );
            }

            RestServiceResult rsrLoad = getServiceRest.get(User.class, userId);
            if(rsrLoad.getHttpStatus() == NOT_FOUND) {
                return RestServiceResult.buildServiceResult(NOT_FOUND, rsrLoad.getErrorMsg(), rsrLoad.getUserMsg());
            }

            dao.merge(userToUpdate);

            return RestServiceResult.buildServiceResult(OK);
        }
    }
}
