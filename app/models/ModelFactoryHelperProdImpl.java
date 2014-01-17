package models;

import com.google.inject.Guice;
import com.google.inject.Injector;
import models.user.User;
import models.user.UserSecurityModule;
import org.jetbrains.annotations.NotNull;

public class ModelFactoryHelperProdImpl implements ModelFactoryHelper {

    @NotNull
    public User userFactory(Long id, String login, String newPassword, String confirmPassword, String email) {

        Injector injector = Guice.createInjector(new UserSecurityModule());
        User user = injector.getInstance(User.class);

        user.setId(id);
        user.setLogin(login);
        user.setNewPassword(newPassword);
        user.setConfirmPassword(confirmPassword);
        user.setEmail(email);

        return user;
    }
}
