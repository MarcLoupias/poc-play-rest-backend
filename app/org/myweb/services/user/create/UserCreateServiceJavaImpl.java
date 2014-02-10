package org.myweb.services.user.create;

import com.google.inject.Inject;
import models.user.User;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.myweb.db.Dao;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.ServiceException;
import org.myweb.services.user.check.UserCheckEmailServiceJava;
import org.myweb.services.user.check.UserCheckLoginServiceJava;
import org.myweb.utils.security.PasswordGenerationService;
import play.i18n.Messages;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static play.mvc.Http.Status.*;

public class UserCreateServiceJavaImpl implements UserCreateServiceJava {

    private final Dao dao;
    private final UserCheckLoginServiceJava checkLogin;
    private final UserCheckEmailServiceJava checkEmail;
    private final PasswordGenerationService passwordGenerationService;

    @Inject
    public UserCreateServiceJavaImpl(
            Dao dao, UserCheckLoginServiceJava checkLogin, UserCheckEmailServiceJava checkEmail,
            PasswordGenerationService passwordGenerationService
    ) {
        this.dao = dao;
        this.checkLogin = checkLogin;
        this.checkEmail = checkEmail;
        this.passwordGenerationService = passwordGenerationService;
    }

    @NotNull
    @Override
    public JavaServiceResult createUser(@NotNull User user) throws ServiceException {

        checkLogin.check(user.getLogin());
        checkEmail.check(user.getEmail());

        if( user.getNewPassword() == null || user.getConfirmPassword() == null ||
                (!user.getNewPassword().equals(user.getConfirmPassword())) ){

            throw new ServiceException(
                    UserCreateServiceJavaImpl.class.getName(), BAD_REQUEST, "input passwords doesn't match",
                    Messages.get("user.create.error.pwd.not.match")
            );
        }

        try {

            user.setUserPasswordSettings(
                    passwordGenerationService.generate(
                            user.getUserPasswordSettings(), user.getNewPassword().toCharArray()
                    )
            );

        } catch (NoSuchAlgorithmException e) {

            throw new ServiceException(
                    UserCreateServiceJavaImpl.class.getName(), INTERNAL_SERVER_ERROR,
                    "Given hash algorithm does not exist or not supported ! (" +
                            user.getUserPasswordSettings().getPwdPBKDF2algo() + ")",
                    Messages.get("user.create.error.internal.server.error")
            );

        } catch (InvalidKeySpecException e) {

            throw new ServiceException(
                    UserCreateServiceJavaImpl.class.getName(), INTERNAL_SERVER_ERROR,
                    ExceptionUtils.getStackTrace(e),
                    Messages.get("user.create.error.internal.server.error")
            );
        }

        user.setNewPassword(null);
        user.setConfirmPassword(null);

        dao.saveNew(user);

        return JavaServiceResult.buildServiceResult(CREATED, user);
    }

}
