package org.myweb.services.user.update;

import com.fasterxml.jackson.databind.JsonNode;
import models.ModelFactoryHelper;
import models.ModelFactoryHelperTestImpl;
import models.user.User;
import models.user.UserPasswordSettings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.Dao;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.user.check.UserCheckEmailServiceJava;
import org.myweb.services.user.check.UserCheckLoginServiceJava;
import org.myweb.utils.test.TestHelper;
import org.myweb.services.RestServiceResult;
import org.myweb.services.crud.get.GetServiceRest;
import org.myweb.utils.security.PasswordGenerationService;
import play.libs.Json;
import play.mvc.Http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserUpdateServiceRestImplTest {
    private Dao mockedDao = mock(Dao.class);
    private ModelFactoryHelper modelFactoryHelper = new ModelFactoryHelperTestImpl();
    private UserCheckLoginServiceJava checkLogin = mock(UserCheckLoginServiceJava.class);
    private UserCheckEmailServiceJava checkEmail = mock(UserCheckEmailServiceJava.class);
    private PasswordGenerationService passwordGenerationService = mock(PasswordGenerationService.class);
    private GetServiceRest getServiceRest = mock(GetServiceRest.class);
    private User user1;

    private UserUpdateServiceRestImpl service;

    @Before
    public void setUp() throws Exception {

        user1 = TestHelper.userFactory(1l, "loginExist", "l3g@lPwd", "l3g@lPwd", "emailexist@toto.fr");

        when(checkLogin.checkExcludingUserId(user1.getLogin(), user1.getId()))
                .thenReturn(JavaServiceResult.buildServiceResult(Http.Status.BAD_REQUEST));
        when(checkLogin.checkExcludingUserId("unusedLogin", user1.getId()))
                .thenReturn(JavaServiceResult.buildServiceResult(Http.Status.OK));

        when(checkEmail.checkExcludingUserId(user1.getEmail(), user1.getId()))
                .thenReturn(JavaServiceResult.buildServiceResult(Http.Status.BAD_REQUEST));
        when(checkEmail.checkExcludingUserId("unused@email.fr", user1.getId()))
                .thenReturn(JavaServiceResult.buildServiceResult(Http.Status.OK));

        UserPasswordSettings userPasswordSettings = TestHelper
                .userPasswordSettingsFactory(new byte[0], 1, 1, "algo", new byte[0], 1);
        when(passwordGenerationService.generate(userPasswordSettings, "password".toCharArray()))
                .thenReturn(userPasswordSettings);

        when(getServiceRest.get(User.class, user1.getId()))
                .thenReturn(RestServiceResult.buildServiceResult(Http.Status.OK));

        service = new UserUpdateServiceRestImpl(
                mockedDao, modelFactoryHelper, checkLogin, checkEmail, passwordGenerationService, getServiceRest
        );
    }

    @Test
    public void test_RestServiceResult_updateUser_BAD_REQUEST_loginAlreadyExist() {

        user1.setEmail("unused@email.fr");
        JsonNode jsUser = Json.toJson(user1);
        RestServiceResult res = service.updateUser(jsUser, user1.getId());

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_updateUser_BAD_REQUEST_emailAlreadyExist() {

        user1.setLogin("unusedLogin");
        JsonNode jsUser = Json.toJson(user1);
        RestServiceResult res = service.updateUser(jsUser, user1.getId());

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_updateUser_OK() {

        user1.setLogin("unusedLogin");
        user1.setEmail("unused@email.fr");
        JsonNode jsUser = Json.toJson(user1);
        RestServiceResult res = service.updateUser(jsUser, user1.getId());

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_updateUser_BAD_REQUEST_badPasswords() {

        user1.setConfirmPassword("l3g@lPwdNotMatching");
        JsonNode jsUser = Json.toJson(user1);
        RestServiceResult res = service.updateUser(jsUser, user1.getId());

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_updateUser_BAD_REQUEST_idMissing() {

        JsonNode jsUser = Json.toJson(user1);
        RestServiceResult res = service.updateUser(jsUser, null);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());

        res = service.updateUser(jsUser, 0l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_updateUser_BAD_REQUEST_urlIdAndPutResourceIdDiffer() {

        JsonNode jsUser = Json.toJson(user1);
        RestServiceResult res = service.updateUser(jsUser, 333l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }
}
