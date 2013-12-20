package org.myweb.services.user;

import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import models.UserPasswordSettings;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.myweb.db.Dao;
import org.myweb.db.TestHelper;
import org.myweb.services.RestServiceResult;
import org.myweb.utils.SecurityUtils;
import play.libs.Json;
import play.mvc.Http;
import play.test.FakeApplication;
import play.test.Helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserLoginServiceTest {
    public static FakeApplication app;
    private Dao mockedDao = mock(Dao.class);
    @SuppressWarnings("UnusedDeclaration")
    private User user;
    @Mock
    private Http.Request request;

    @Before
    public void setUp() throws Exception {

        app = Helpers.fakeApplication();
        Helpers.start(app);

        Map<String, String> sessionData = Collections.emptyMap();
        Map<String, String> flashData = Collections.emptyMap();
        Map<String, Object> args = Collections.emptyMap();
        Http.Context context = new Http.Context(1l, null, request, sessionData, flashData, args);
        context.changeLang("en");
        Http.Context.current.set(context);

        byte[] hashedPwd = new byte[]{
                110, 125, -69, -105, -48, 42, -57, 77, 15, -120, -123, 88, -123, 89, -123, 35, -99, 56, 74, 55, -54, -34, -110, 119, -65, -80,
                -120, 57, 50, -70, -85, 90
        };
        byte[] salt = new byte[]{74, -117, -108, -5, -48, 102, -117, -57, 69, 121, -125, 30, 5, 116, 2, 72};
        UserPasswordSettings userPasswordSettings = TestHelper.userPasswordSettingsFactory(
                hashedPwd, 10000, 256, SecurityUtils.PBKDF2WithHmacSHA1, salt, 16
        );
        User user = TestHelper.userFactory(1l, "loginExist", "l3g@lPwd", "l3g@lPwd", "emailexist@toto.fr", userPasswordSettings);

        Map<String, Object> paramLogin = new HashMap<>();
        paramLogin.put("login", "loginNotFound");
        when(mockedDao.namedQuerySingleResult("User.findByLogin", User.class, paramLogin)).thenReturn(
                null
        );

        Map<String, Object> paramLoginExist = new HashMap<>();
        paramLoginExist.put("login", "loginExist");
        when(mockedDao.namedQuerySingleResult("User.findByLogin", User.class, paramLoginExist)).thenReturn(
                user
        );

        Map<String, Object> paramEmail = new HashMap<>();
        paramEmail.put("email", "emailnotfound@toto.fr");
        when(mockedDao.namedQuerySingleResult("User.findByEmail", User.class, paramEmail)).thenReturn(
                null
        );

        Map<String, Object> paramEmailExist = new HashMap<>();
        paramEmailExist.put("email", "emailexist@toto.fr");
        when(mockedDao.namedQuerySingleResult("User.findByEmail", User.class, paramEmailExist)).thenReturn(
                user
        );
    }

    @After
    public void setDown() {
        Helpers.stop(app);
    }

    @Test
    public void test_RestServiceResult_loginUser_OK() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory("emailexist@toto.fr", "loginExist", "l3g@lPwd")
        );

        RestServiceResult res = UserLoginService.getInstance(mockedDao).loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_loginUser_UNAUTHORIZED() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory("emailexist@toto.fr", "loginExist", "wrong_l3g@lPwd")
        );

        RestServiceResult res = UserLoginService.getInstance(mockedDao).loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.UNAUTHORIZED, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_loginUser_OK_byLogin() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory("", "loginExist", "l3g@lPwd")
        );

        RestServiceResult res = UserLoginService.getInstance(mockedDao).loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_loginUser_OK_byEmail() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory("emailexist@toto.fr", "", "l3g@lPwd")
        );

        RestServiceResult res = UserLoginService.getInstance(mockedDao).loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_loginUser_NOT_FOUND() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory("emailnotfound@toto.fr", "loginNotFound", "l3g@lPwd")
        );

        RestServiceResult res = UserLoginService.getInstance(mockedDao).loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_loginUser_NOT_FOUND_byLogin() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory("", "loginNotFound", "l3g@lPwd")
        );

        RestServiceResult res = UserLoginService.getInstance(mockedDao).loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_loginUser_NOT_FOUND_byEmail() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory("emailnotfound@toto.fr", "", "l3g@lPwd")
        );

        RestServiceResult res = UserLoginService.getInstance(mockedDao).loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_loginUser_BAD_REQUEST_malformedPassword() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory("emailexist@toto.fr", "loginExist", "leg@lPwd")
        );

        RestServiceResult res = UserLoginService.getInstance(mockedDao).loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_loginUser_BAD_REQUEST_loginAndEmailMissing() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory(null, null, "l3g@lPwd")
        );

        RestServiceResult res = UserLoginService.getInstance(mockedDao).loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());

        jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory("", "", "l3g@lPwd")
        );

        res = UserLoginService.getInstance(mockedDao).loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }

}
