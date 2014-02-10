package org.myweb.services.user.update;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.ModelFactoryHelper;
import models.user.User;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.Dao;
import org.myweb.services.RestServiceResult;
import org.myweb.services.ServiceException;
import org.myweb.services.crud.get.GetServiceRest;
import org.myweb.services.user.check.UserCheckEmailServiceJava;
import org.myweb.services.user.check.UserCheckLoginServiceJava;
import org.myweb.utils.security.PasswordGenerationService;
import play.data.Form;
import play.i18n.Messages;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static play.mvc.Http.Status.*;

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
    public RestServiceResult updateUser(@NotNull JsonNode jsContent, @Nullable Long userId) throws ServiceException {

        if(userId == null || userId < 1l) {

            throw new ServiceException(
                    UserUpdateServiceRestImpl.class.getName(), BAD_REQUEST, "put request without user id",
                    Messages.get("user.update.error.id.missing")
            );
        }

        Form<User> userForm = Form.form(User.class);
        userForm = userForm.bind(jsContent);

        if(userForm.hasErrors()) {

            throw new ServiceException(
                    UserUpdateServiceRestImpl.class.getName(), BAD_REQUEST, userForm.errorsAsJson().asText(),
                    "user msg", userForm.errorsAsJson());

        } else {
            User formUser = userForm.bind(jsContent).get();

            if(formUser.getId() == null || (formUser.getId().longValue() != userId.longValue()) ){

                throw new ServiceException(
                        UserUpdateServiceRestImpl.class.getName(), BAD_REQUEST,
                        "put request id (" + userId + ") differ from put content id (" + formUser.getId() + ")",
                        Messages.get("user.update.error.url.id.and.pojo.id.differ")
                );
            }

            checkLogin.checkExcludingUserId(formUser.getLogin(), formUser.getId());
            checkEmail.checkExcludingUserId(formUser.getEmail(), formUser.getId());

            if( formUser.getNewPassword() == null || formUser.getConfirmPassword() == null ||
                    (!formUser.getNewPassword().equals(formUser.getConfirmPassword())) ){

                throw new ServiceException(
                        UserUpdateServiceRestImpl.class.getName(), BAD_REQUEST,
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

                throw new ServiceException(
                        UserUpdateServiceRestImpl.class.getName(), INTERNAL_SERVER_ERROR,
                        "Given hash algorithm does not exist or not supported ! (" +
                                userToUpdate.getUserPasswordSettings().getPwdPBKDF2algo() + ")",
                        Messages.get("user.update.error.internal.server.error")
                );

            } catch (InvalidKeySpecException e) {

                throw new ServiceException(
                        UserUpdateServiceRestImpl.class.getName(), INTERNAL_SERVER_ERROR,
                        ExceptionUtils.getStackTrace(e),
                        Messages.get("user.update.error.internal.server.error")
                );
            }

            getServiceRest.get(User.class, userId);

            dao.merge(userToUpdate);

            return RestServiceResult.buildServiceResult(OK);
        }
    }
}
