package models;


import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.TestHelper;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;

import java.util.List;
import java.util.Map;

public class UserValidationTest {

    User validUser;

    @Before
    public void setUp() throws Exception {

        validUser = TestHelper.userFactory(null, "totoLogin", "l3g@lPwd", "l3g@lPwd", "toto@toto.fr");
    }

    @Test
    public void test_UserValidation_OK() {

        JsonNode jsUser = Json.toJson(validUser);

        Form<User> userForm = Form.form(User.class);
        userForm = userForm.bind(jsUser);

        Assert.assertFalse(userForm.hasErrors());
    }

    @Test
    public void test_UserValidation_KO_requiredFields() {

        //noinspection ConstantConditions
        validUser.setLogin(null);
        //noinspection ConstantConditions
        validUser.setEmail(null);
        validUser.setNewPassword("illegalPassword");
        validUser.setConfirmPassword("illegalPassword");

        JsonNode jsUser = Json.toJson(validUser);

        Form<User> userForm = Form.form(User.class);
        userForm = userForm.bind(jsUser);

        Assert.assertTrue(userForm.hasErrors());

        Map<String,List<ValidationError>> errors = userForm.errors();
        Assert.assertNotNull(errors);
        Assert.assertTrue(errors.size() > 0);

        List<ValidationError> loginErrors = errors.get("login");
        Assert.assertNotNull(loginErrors);
        Assert.assertTrue(loginErrors.size() > 0);
        ValidationError loginError = loginErrors.get(0);
        Assert.assertEquals("error.required", loginError.message());

        List<ValidationError> emailErrors = errors.get("email");
        Assert.assertNotNull(emailErrors);
        Assert.assertTrue(emailErrors.size() > 0);
        ValidationError emailError = emailErrors.get(0);
        Assert.assertEquals("error.required", emailError.message());

        List<ValidationError> newPasswordErrors = errors.get("newPassword");
        Assert.assertNotNull(newPasswordErrors);
        Assert.assertTrue(newPasswordErrors.size() > 0);
        ValidationError newPasswordError = newPasswordErrors.get(0);
        Assert.assertEquals("error.pattern", newPasswordError.message());

        List<ValidationError> confirmPasswordErrors = errors.get("confirmPassword");
        Assert.assertNotNull(confirmPasswordErrors);
        Assert.assertTrue(confirmPasswordErrors.size() > 0);
        ValidationError confirmPasswordError = confirmPasswordErrors.get(0);
        Assert.assertEquals("error.pattern", confirmPasswordError.message());
    }
}
