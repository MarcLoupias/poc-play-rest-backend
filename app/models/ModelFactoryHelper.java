package models;

import models.user.User;
import org.jetbrains.annotations.NotNull;

public interface ModelFactoryHelper {

    @NotNull
    public User userFactory(Long id, String login, String newPassword, String confirmPassword, String email);

}
