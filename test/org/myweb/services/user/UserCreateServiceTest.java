package org.myweb.services.user;

import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.Dao;
import org.myweb.db.TestHelper;
import org.myweb.services.RestServiceResult;
import play.libs.Json;
import play.mvc.Http;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

public class UserCreateServiceTest {
    private Dao mockedDao = mock(Dao.class);

    @Before
    public void setUp() throws Exception {

        Map<String, Object> paramLogin = new HashMap<>();
        paramLogin.put("login", "totoLogin");
        when(mockedDao.namedQuerySingleResult("User.findByLogin", User.class, paramLogin)).thenReturn(
                null
        );

        Map<String, Object> paramLoginExist = new HashMap<>();
        paramLoginExist.put("login", "loginExist");
        when(mockedDao.namedQuerySingleResult("User.findByLogin", User.class, paramLoginExist)).thenReturn(
                TestHelper.userFactory(1l, "loginExist", "l3g@lPwd", "l3g@lPwd", "email@toto.fr")
        );

        Map<String, Object> paramEmail = new HashMap<>();
        paramEmail.put("email", "toto@toto.fr");
        when(mockedDao.namedQuerySingleResult("User.findByEmail", User.class, paramEmail)).thenReturn(
                null
        );

        Map<String, Object> paramEmailExist = new HashMap<>();
        paramEmailExist.put("email", "emailexist@toto.fr");
        when(mockedDao.namedQuerySingleResult("User.findByEmail", User.class, paramEmailExist)).thenReturn(
                TestHelper.userFactory(1l, "loginExist", "l3g@lPwd", "l3g@lPwd", "emailexist@toto.fr")
        );
    }

    @Test
    public void test_RestServiceResult_createUser_BAD_REQUEST_loginAlreadyExist() {

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                User newUser = TestHelper.userFactory(null, "loginExist", "l3g@lPwd", "l3g@lPwd", "toto@toto.fr");
                JsonNode jsNewUser = Json.toJson(newUser);
                RestServiceResult res = UserCreateService.getInstance(mockedDao).createUser(jsNewUser);

                Assert.assertNotNull(res);
                Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
            }
        });
    }

    @Test
    public void test_RestServiceResult_createUser_BAD_REQUEST_emailAlreadyExist() {

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                User newUser = TestHelper.userFactory(null, "totoLogin", "l3g@lPwd", "l3g@lPwd", "emailexist@toto.fr");
                JsonNode jsNewUser = Json.toJson(newUser);
                RestServiceResult res = UserCreateService.getInstance(mockedDao).createUser(jsNewUser);

                Assert.assertNotNull(res);
                Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
            }
        });
    }

    @Test
    public void test_RestServiceResult_createUser_CREATED() {

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                User newUser = TestHelper.userFactory(null, "totoLogin", "l3g@lPwd", "l3g@lPwd", "toto@toto.fr");
                JsonNode jsNewUser = Json.toJson(newUser);
                RestServiceResult res = UserCreateService.getInstance(mockedDao).createUser(jsNewUser);

                Assert.assertNotNull(res);
                Assert.assertEquals(Http.Status.CREATED, res.getHttpStatus());
            }
        });
    }

    @Test
    public void test_RestServiceResult_createUser_BAD_REQUEST_badPasswords() {

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                User newUser = TestHelper.userFactory(null, "totoLogin", "l3g@lPwd", "l3g@lPwdNotMatching", "toto@toto.fr");
                JsonNode jsNewUser = Json.toJson(newUser);
                RestServiceResult res = UserCreateService.getInstance(mockedDao).createUser(jsNewUser);

                Assert.assertNotNull(res);
                Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
            }
        });
    }
}
