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

public class UserCheckEmailServiceJavaImplTest {
    private DaoJpa mockedDao = mock(DaoJpa.class);
    private UserCheckEmailServiceJavaImpl restService;

    @Before
    public void setUp() throws Exception {

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

        Map<String, Object> paramEmailExclId = new HashMap<>();
        paramEmailExclId.put("email", "unused@email.fr");
        paramEmailExclId.put("excludedId", 1l);
        when(mockedDao.namedQuerySingleResult("User.findByEmailExcludingId", User.class, paramEmailExclId)).thenReturn(
                null
        );

        Map<String, Object> paramEmailExistExclId = new HashMap<>();
        paramEmailExistExclId.put("email", "emailexist@toto.fr");
        paramEmailExistExclId.put("excludedId", 1l);
        when(mockedDao.namedQuerySingleResult("User.findByEmailExcludingId", User.class, paramEmailExistExclId)).thenReturn(
                TestHelper.userFactory(1l, "loginExist", "l3g@lPwd", "l3g@lPwd", "emailexist@toto.fr")
        );

        restService = new UserCheckEmailServiceJavaImpl(mockedDao);
    }

    @Test
    public void test_JavaServiceResult_check_OK() {
        JavaServiceResult res = restService.check("toto@toto.fr");

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }

    @Test
    public void test_JavaServiceResult_check_BAD_REQUEST() {
        JavaServiceResult res = restService.check("emailexist@toto.fr");

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }

    @Test
    public void test_JavaServiceResult_checkExcludingUserId_OK() {
        JavaServiceResult res = restService.checkExcludingUserId("unused@email.fr", 1l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }

    @Test
    public void test_JavaServiceResult_checkExcludingUserId_BAD_REQUEST() {
        JavaServiceResult res = restService.checkExcludingUserId("emailexist@toto.fr", 1l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }
}
