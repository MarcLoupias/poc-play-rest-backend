package org.myweb.services.user.logout;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.services.RestServiceResult;
import org.myweb.utils.session.SessionUtilsService;
import play.mvc.Http;

import static org.mockito.Mockito.mock;

public class UserLogoutServiceRestImplTest {

    private SessionUtilsService sessionUtilsService = mock(SessionUtilsService.class);
    private UserLogoutServiceRestImpl service;

    @Before
    public void setUp() throws Exception {
        service = new UserLogoutServiceRestImpl(sessionUtilsService);
    }

    @Test
    public void test_RestServiceResult_logoutUser_OK() {

        RestServiceResult res = service.logoutUser();

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }
}
