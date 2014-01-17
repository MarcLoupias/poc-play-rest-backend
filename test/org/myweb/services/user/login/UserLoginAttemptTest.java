package org.myweb.services.user.login;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.utils.test.TestHelper;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;

import java.util.List;
import java.util.Map;

public class UserLoginAttemptTest {

    UserLoginAttempt validLoginAttempt;

    @Before
    public void setUp() throws Exception {
        validLoginAttempt = TestHelper.userLoginAttemptFactory("toto@toto.fr", "totoLogin", "l3g@lPwd");
    }

    @Test
    public void test_UserLoginAttemptValidation_OK() {

        JsonNode jsUserLoginAttempt = Json.toJson(validLoginAttempt);

        Form<UserLoginAttempt> userLoginAttemptForm = Form.form(UserLoginAttempt.class);
        userLoginAttemptForm = userLoginAttemptForm.bind(jsUserLoginAttempt);

        Assert.assertFalse(userLoginAttemptForm.hasErrors());
    }

    @Test
    public void test_UserLoginAttemptValidation_KO_requiredFields() {

        // testing that required password is well detected

        UserLoginAttempt loginAttemptBadPwd = new UserLoginAttempt();
        loginAttemptBadPwd.setEmail(validLoginAttempt.getEmail());
        loginAttemptBadPwd.setLogin(validLoginAttempt.getLogin());
        //noinspection ConstantConditions
        loginAttemptBadPwd.setPassword(null);

        JsonNode jsUserLoginAttempt = Json.toJson(loginAttemptBadPwd);

        Form<UserLoginAttempt> userLoginAttemptForm = Form.form(UserLoginAttempt.class);
        userLoginAttemptForm = userLoginAttemptForm.bind(jsUserLoginAttempt);

        Assert.assertTrue(userLoginAttemptForm.hasErrors());

        Map<String,List<ValidationError>> errors = userLoginAttemptForm.errors();
        Assert.assertNotNull(errors);
        Assert.assertTrue(errors.size() > 0);

        List<ValidationError> passwordErrors = errors.get("password");
        Assert.assertNotNull(passwordErrors);
        Assert.assertTrue(passwordErrors.size() > 0);
        ValidationError passwordError = passwordErrors.get(0);
        Assert.assertEquals("error.required", passwordError.message());

        // testing that required mail or login are well detected when both are missing

        UserLoginAttempt loginAttemptMailAndLoginMissing = new UserLoginAttempt();
        loginAttemptMailAndLoginMissing.setEmail(null);
        loginAttemptMailAndLoginMissing.setLogin(null);
        loginAttemptMailAndLoginMissing.setPassword(validLoginAttempt.getPassword());

        jsUserLoginAttempt = Json.toJson(loginAttemptMailAndLoginMissing);
        userLoginAttemptForm = Form.form(UserLoginAttempt.class);
        userLoginAttemptForm = userLoginAttemptForm.bind(jsUserLoginAttempt);

        Assert.assertTrue(userLoginAttemptForm.hasErrors());

        errors = userLoginAttemptForm.errors();
        Assert.assertNotNull(errors);
        Assert.assertTrue(errors.size() > 0);

        List<ValidationError> loginAndEmailErrors = errors.get("loginOrEmail");
        Assert.assertNotNull(loginAndEmailErrors);
        Assert.assertTrue(loginAndEmailErrors.size() > 0);
        ValidationError loginAndEmailError = loginAndEmailErrors.get(0);
        Assert.assertEquals("error.required", loginAndEmailError.message());
    }

    @Test
    public void test_UserLoginAttemptValidation_KO_patterns() {
        UserLoginAttempt loginAttemptBadPwd = new UserLoginAttempt();
        loginAttemptBadPwd.setEmail(validLoginAttempt.getEmail());
        loginAttemptBadPwd.setLogin(validLoginAttempt.getLogin());
        loginAttemptBadPwd.setPassword("illegalPassword");

        JsonNode jsUserLoginAttempt = Json.toJson(loginAttemptBadPwd);

        Form<UserLoginAttempt> userLoginAttemptForm = Form.form(UserLoginAttempt.class);
        userLoginAttemptForm = userLoginAttemptForm.bind(jsUserLoginAttempt);

        Assert.assertTrue(userLoginAttemptForm.hasErrors());

        Map<String,List<ValidationError>> errors = userLoginAttemptForm.errors();
        Assert.assertNotNull(errors);
        Assert.assertTrue(errors.size() > 0);

        List<ValidationError> passwordErrors = errors.get("password");
        Assert.assertNotNull(passwordErrors);
        Assert.assertTrue(passwordErrors.size() > 0);
        ValidationError passwordError = passwordErrors.get(0);
        Assert.assertEquals("error.pattern", passwordError.message());
    }
}
