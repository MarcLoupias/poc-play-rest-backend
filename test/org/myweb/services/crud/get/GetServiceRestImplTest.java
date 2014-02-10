package org.myweb.services.crud.get;

import com.fasterxml.jackson.databind.JsonNode;
import models.County;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.DaoJpa;
import org.myweb.services.ServiceException;
import org.myweb.utils.rest.FilterParserService;
import org.myweb.utils.rest.FilterParserServiceImpl;
import org.myweb.utils.test.TestHelper;
import org.myweb.services.RestServiceResult;
import play.libs.Json;
import play.mvc.Http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetServiceRestImplTest {
    private DaoJpa mockedDao = mock(DaoJpa.class);
    private County countyA;
    private GetServiceRestImpl restService;
    private FilterParserService filterParserService = new FilterParserServiceImpl();

    @Before
    public void setUp() throws Exception {

        countyA = TestHelper.countyFactory(1l, "46", "Lot");

        when(mockedDao.load(County.class, 1l)).thenReturn(
                countyA
        );

        GetServiceJavaImpl javaService = new GetServiceJavaImpl(mockedDao, filterParserService);
        restService = new GetServiceRestImpl(javaService);
    }

    @Test
    public void test_RestServiceResult_load_NOT_FOUND() {

        boolean exception = false;

        try {
            restService.get(County.class, 666l);
        } catch (ServiceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(Http.Status.NOT_FOUND, e.getHttpStatus());
            exception = true;
        }

        Assert.assertTrue("exception should be true", exception);
    }

    @Test
    public void test_RestServiceResult_load_OK() {
        RestServiceResult res = null;
        try {
            res = restService.get(County.class, countyA.getId());
        } catch (ServiceException e) {
            Assert.fail();
        }

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());

        JsonNode jsCounty = res.getJsContent();
        County c = Json.fromJson(jsCounty, County.class);

        Assert.assertNotNull(c);
        Assert.assertEquals(c.getName(), countyA.getName());
    }
}
