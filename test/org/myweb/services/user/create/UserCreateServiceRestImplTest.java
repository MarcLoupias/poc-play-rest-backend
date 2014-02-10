package org.myweb.services.user.create;

import com.fasterxml.jackson.databind.JsonNode;
import models.ModelFactoryHelper;
import models.ModelFactoryHelperTestImpl;
import models.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.ServiceException;
import org.myweb.utils.test.TestHelper;
import play.libs.Json;
import play.mvc.Http;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserCreateServiceRestImplTest {

    private UserCreateServiceJava userCreateServiceJava = mock(UserCreateServiceJava.class);
    private ModelFactoryHelper modelFactoryHelper = new ModelFactoryHelperTestImpl();
    private UserCreateServiceRestImpl service;
    private User newUser;

    @Before
    public void setUp() throws Exception {
        newUser = TestHelper.userFactory(null, "totoLogin", "l3g@lPwd", "l3g@lPwd", "toto@toto.fr");
        when(userCreateServiceJava.createUser(isA(User.class)))
                .thenReturn(JavaServiceResult.buildServiceResult(Http.Status.CREATED, newUser));

        service = new UserCreateServiceRestImpl(userCreateServiceJava, modelFactoryHelper);
    }

    @Test
    public void test_JavaServiceResult_createUser_CREATED() {

        JsonNode jsNewUser = Json.toJson(newUser);
        RestServiceResult res = null;
        try {
            res = service.createUser(jsNewUser);
        } catch (ServiceException e) {
            Assert.fail();
        }

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.CREATED, res.getHttpStatus());
    }

}
