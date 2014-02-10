package org.myweb.services.crud.create;


import com.fasterxml.jackson.databind.JsonNode;
import models.County;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.Dao;
import org.myweb.services.ServiceException;
import org.myweb.utils.test.TestHelper;
import org.myweb.services.RestServiceResult;
import play.libs.Json;
import play.mvc.Http;

import static org.mockito.Mockito.mock;

public class CreateServiceRestImplTest {
    private Dao mockedDao = mock(Dao.class);
    private CreateServiceRestImpl restService;

    @Before
    public void setUp() throws Exception {
        CreateServiceJavaImpl javaService = new CreateServiceJavaImpl(mockedDao);
        restService = new CreateServiceRestImpl(javaService);
    }

    @Test
    public void test_RestServiceResult_create_CREATED() {
        County newCounty = TestHelper.countyFactory(null, "46", "Lot");
        JsonNode jsNewCounty = Json.toJson(newCounty);
        RestServiceResult res = null;

        try {
            res = restService.create(County.class, jsNewCounty);
        } catch (ServiceException e) {
            Assert.fail();
        }

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.CREATED, res.getHttpStatus());
    }
}
