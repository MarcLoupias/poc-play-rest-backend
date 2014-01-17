package org.myweb.services.user.login;

import com.fasterxml.jackson.databind.JsonNode;
import models.ModelFactoryHelper;
import models.ModelFactoryHelperTestImpl;
import models.user.User;
import models.user.UserPasswordSettings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.Dao;
import org.myweb.utils.exception.ExceptionUtilsServiceImpl;
import org.myweb.utils.security.SecurityUtilsService;
import org.myweb.utils.session.SessionUtilsService;
import org.myweb.utils.test.TestHelper;
import org.myweb.services.RestServiceResult;
import play.libs.Json;
import play.mvc.Http;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserLoginServiceRestImplTest {

    private Dao mockedDao = mock(Dao.class);
    private SecurityUtilsService securityUtilsService = mock(SecurityUtilsService.class);
    private SessionUtilsService sessionUtilsService = mock(SessionUtilsService.class);

    private ModelFactoryHelper modelFactoryHelper = new ModelFactoryHelperTestImpl();

    private UserLoginServiceRestImpl service;

    @SuppressWarnings("UnusedDeclaration")
    private User user;

    @Before
    public void setUp() throws Exception {

        user = modelFactoryHelper.userFactory(1l, "loginExist", "l3g@lPwd", "l3g@lPwd", "emailexist@toto.fr");

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

        UserPasswordSettings settings = user.getUserPasswordSettings();
        String userPassword = user.getNewPassword();
        assert userPassword != null;
        when(securityUtilsService.isExpectedPassword(
                userPassword.toCharArray(), settings.getSalt(), settings.getHashedPwd(), settings.getIterations(),
                settings.getPwdDerivatedKeyBitLength(), settings.getPwdPBKDF2algo())
        ).thenReturn(true);

        service = new UserLoginServiceRestImpl(
                mockedDao, new ExceptionUtilsServiceImpl(), securityUtilsService, sessionUtilsService
        );
    }

    @Test
    public void test_RestServiceResult_loginUser_OK() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory(user.getEmail(), user.getLogin(), user.getNewPassword())
        );

        RestServiceResult res = service.loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_loginUser_UNAUTHORIZED() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory(user.getEmail(), user.getLogin(), "wrong_l3g@lPwd")
        );

        RestServiceResult res = service.loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.UNAUTHORIZED, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_loginUser_OK_byLogin() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory("", user.getLogin(), user.getNewPassword())
        );

        RestServiceResult res = service.loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_loginUser_OK_byEmail() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory(user.getEmail(), "", user.getNewPassword())
        );

        RestServiceResult res = service.loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_loginUser_NOT_FOUND() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory("emailnotfound@toto.fr", "loginNotFound", user.getNewPassword())
        );

        RestServiceResult res = service.loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_loginUser_NOT_FOUND_byLogin() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory("", "loginNotFound", user.getNewPassword())
        );

        RestServiceResult res = service.loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_loginUser_NOT_FOUND_byEmail() {

        JsonNode jsUserLoginAttempt = Json.toJson(
                TestHelper.userLoginAttemptFactory("emailnotfound@toto.fr", "", user.getNewPassword())
        );

        RestServiceResult res = service.loginUser(jsUserLoginAttempt);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NOT_FOUND, res.getHttpStatus());
    }
}
