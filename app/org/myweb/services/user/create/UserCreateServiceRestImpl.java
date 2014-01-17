package org.myweb.services.user.create;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.ModelFactoryHelper;
import models.user.User;
import org.jetbrains.annotations.NotNull;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import play.data.Form;
import play.libs.Json;

import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.CREATED;

public class UserCreateServiceRestImpl implements UserCreateServiceRest {

    private final UserCreateServiceJava userCreateServiceJava;
    private final ModelFactoryHelper modelFactoryHelper;

    @Inject
    public UserCreateServiceRestImpl(UserCreateServiceJava userCreateServiceJava, ModelFactoryHelper modelFactoryHelper) {
        this.userCreateServiceJava = userCreateServiceJava;
        this.modelFactoryHelper = modelFactoryHelper;
    }

    @NotNull
    @Override
    public RestServiceResult createUser(@NotNull JsonNode jsContent) {

        Form<User> userForm = Form.form(User.class);
        userForm = userForm.bind(jsContent);

        if(userForm.hasErrors()) {

            return RestServiceResult.buildServiceResult(BAD_REQUEST, userForm.errorsAsJson());

        } else {
            User formUser = userForm.bind(jsContent).get();

            User newUser = modelFactoryHelper.userFactory(
                    null, formUser.getLogin(), formUser.getNewPassword(),
                    formUser.getConfirmPassword(), formUser.getEmail()
            );

            JavaServiceResult res = userCreateServiceJava.createUser(newUser);

            if(res.getHttpStatus() != CREATED) {
                return RestServiceResult.buildServiceResult(res);
            }

            newUser = (User) res.getSingleContent();
            assert newUser != null;
            newUser.setNewPassword(null);
            newUser.setConfirmPassword(null);

            return RestServiceResult.buildServiceResult(CREATED, Json.toJson(newUser));
        }

    }
}
