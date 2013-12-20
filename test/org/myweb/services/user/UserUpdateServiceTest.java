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

public class UserUpdateServiceTest {
    private Dao mockedDao = mock(Dao.class);
    private User user1;


    @Before
    public void setUp() throws Exception {

        user1 = TestHelper.userFactory(1l, "loginExist", "l3g@lPwd", "l3g@lPwd", "emailexist@toto.fr");

        Map<String, Object> paramLogin = new HashMap<>();
        paramLogin.put("login", "unusedLogin");
        paramLogin.put("excludedId", 1l);
        when(mockedDao.namedQuerySingleResult("User.findByLoginExcludingId", User.class, paramLogin)).thenReturn(
                null
        );

        Map<String, Object> paramLoginExist = new HashMap<>();
        paramLoginExist.put("login", "loginExist");
        paramLoginExist.put("excludedId", 1l);
        when(mockedDao.namedQuerySingleResult("User.findByLoginExcludingId", User.class, paramLoginExist)).thenReturn(
                user1
        );

        Map<String, Object> paramEmail = new HashMap<>();
        paramEmail.put("email", "unused@email.fr");
        paramEmail.put("excludedId", 1l);
        when(mockedDao.namedQuerySingleResult("User.findByEmailExcludingId", User.class, paramEmail)).thenReturn(
                null
        );

        Map<String, Object> paramEmailExist = new HashMap<>();
        paramEmailExist.put("email", "emailexist@toto.fr");
        paramEmailExist.put("excludedId", 1l);
        when(mockedDao.namedQuerySingleResult("User.findByEmailExcludingId", User.class, paramEmailExist)).thenReturn(
                user1
        );

        when(mockedDao.load(User.class, 1l)).thenReturn(
                user1
        );
    }

    @Test
    public void test_RestServiceResult_updateUser_BAD_REQUEST_loginAlreadyExist() {

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                user1.setEmail("unused@email.fr");
                JsonNode jsUser = Json.toJson(user1);
                RestServiceResult res = UserUpdateService.getInstance(mockedDao).updateUser(jsUser, user1.getId());

                Assert.assertNotNull(res);
                Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
            }
        });
    }

    @Test
    public void test_RestServiceResult_updateUser_BAD_REQUEST_emailAlreadyExist() {

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                user1.setLogin("unusedLogin");
                JsonNode jsUser = Json.toJson(user1);
                RestServiceResult res = UserUpdateService.getInstance(mockedDao).updateUser(jsUser, user1.getId());

                Assert.assertNotNull(res);
                Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
            }
        });
    }

    @Test
    public void test_RestServiceResult_updateUser_OK() {

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                user1.setLogin("unusedLogin");
                user1.setEmail("unused@email.fr");
                JsonNode jsUser = Json.toJson(user1);
                RestServiceResult res = UserUpdateService.getInstance(mockedDao).updateUser(jsUser, user1.getId());

                Assert.assertNotNull(res);
                Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
            }
        });
    }

    @Test
    public void test_RestServiceResult_updateUser_BAD_REQUEST_badPasswords() {

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                user1.setConfirmPassword("l3g@lPwdNotMatching");
                JsonNode jsUser = Json.toJson(user1);
                RestServiceResult res = UserUpdateService.getInstance(mockedDao).updateUser(jsUser, user1.getId());

                Assert.assertNotNull(res);
                Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
            }
        });
    }

    @Test
    public void test_RestServiceResult_updateUser_BAD_REQUEST_idMissing() {

        JsonNode jsUser = Json.toJson(user1);
        RestServiceResult res = UserUpdateService.getInstance(mockedDao).updateUser(jsUser, null);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());

        res = UserUpdateService.getInstance(mockedDao).updateUser(jsUser, 0l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_updateUser_BAD_REQUEST_urlIdAndPutResourceIdDiffer() {

        JsonNode jsUser = Json.toJson(user1);
        RestServiceResult res = UserUpdateService.getInstance(mockedDao).updateUser(jsUser, 333l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }
}
