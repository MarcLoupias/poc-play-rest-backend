package org.myweb.services.user.check;

import models.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.DaoJpa;
import org.myweb.services.JavaServiceResult;
import org.myweb.utils.test.TestHelper;
import play.mvc.Http;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserCheckLoginServiceJavaImplTest {
    private DaoJpa mockedDao = mock(DaoJpa.class);
    private UserCheckLoginServiceJavaImpl restService;

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

        Map<String, Object> paramLoginExclId = new HashMap<>();
        paramLoginExclId.put("login", "unusedLogin");
        paramLoginExclId.put("excludedId", 1l);
        when(mockedDao.namedQuerySingleResult("User.findByLoginExcludingId", User.class, paramLoginExclId)).thenReturn(
                null
        );

        Map<String, Object> paramLoginExistExclId = new HashMap<>();
        paramLoginExistExclId.put("login", "loginExist");
        paramLoginExistExclId.put("excludedId", 1l);
        when(mockedDao.namedQuerySingleResult("User.findByLoginExcludingId", User.class, paramLoginExistExclId)).thenReturn(
                TestHelper.userFactory(1l, "loginExist", "l3g@lPwd", "l3g@lPwd", "email@toto.fr")
        );

        restService = new UserCheckLoginServiceJavaImpl(mockedDao);
    }

    @Test
    public void test_JavaServiceResult_check_OK() {
        JavaServiceResult res = restService.check("totoLogin");

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }

    @Test
    public void test_JavaServiceResult_check_BAD_REQUEST() {
        JavaServiceResult res = restService.check("loginExist");

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }

    @Test
    public void test_JavaServiceResult_checkExcludingUserId_OK() {
        JavaServiceResult res = restService.checkExcludingUserId("unusedLogin", 1l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }

    @Test
    public void test_JavaServiceResult_checkExcludingUserId_BAD_REQUEST() {
        JavaServiceResult res = restService.checkExcludingUserId("loginExist", 1l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }
}
