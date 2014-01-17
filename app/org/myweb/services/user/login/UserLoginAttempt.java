package org.myweb.services.user.login;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

public class UserLoginAttempt {

    @Nullable
    @Constraints.MinLength(value = 1)
    @Constraints.MaxLength(value = 255)
    private String login;

    @Nullable
    @Constraints.Email
    private String email;

    @NotNull
    @Constraints.Required
    @Constraints.Pattern(value="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
    private String password;

    @Nullable
    public String getLogin() {
        return login;
    }

    public void setLogin(@Nullable String login) {
        this.login = login;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public UserLoginAttempt() {
    }

    @SuppressWarnings("UnusedDeclaration")
    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();

        if ( (this.login == null || this.login.isEmpty()) && (this.email == null || this.email.isEmpty()) ) {
            errors.add(new ValidationError("loginOrEmail", "error.required"));
        }

        return errors.isEmpty() ? null : errors;
    }
}
