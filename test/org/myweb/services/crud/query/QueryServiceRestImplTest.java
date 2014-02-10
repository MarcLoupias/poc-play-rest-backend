package org.myweb.services.crud.query;

import com.fasterxml.jackson.databind.JsonNode;
import models.County;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.DaoJpa;
import org.myweb.utils.rest.FilterParserService;
import org.myweb.utils.rest.FilterParserServiceImpl;
import org.myweb.utils.test.TestHelper;
import org.myweb.services.RestServiceResult;
import play.libs.Json;
import play.mvc.Http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueryServiceRestImplTest {
    private DaoJpa mockedDao = mock(DaoJpa.class);
    private County countyA, countyB;
    private QueryServiceRestImpl restService;
    private FilterParserService filterParserService = new FilterParserServiceImpl();

    @Before
    public void setUp() throws Exception {

        countyA = TestHelper.countyFactory(1l, "46", "Lot");
        countyB = TestHelper.countyFactory(2l, "47", "Lot-et-Garonne");

        when(mockedDao.load(County.class)).thenReturn(
                TestHelper.countyListFactory(countyA, countyB)
        );

        QueryServiceJavaImpl javaService = new QueryServiceJavaImpl(mockedDao, filterParserService);
        restService = new QueryServiceRestImpl(javaService, filterParserService);
    }

    @Test
    public void test_RestServiceResult_query_OK() {
        RestServiceResult res = restService.query(County.class);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());

        JsonNode jsCountyList = res.getJsContent();
        Assert.assertNotNull(jsCountyList);
        Assert.assertTrue(jsCountyList.isArray());
        Assert.assertEquals(2, jsCountyList.size());

        for(JsonNode jsCounty : jsCountyList) {
            County c = Json.fromJson(jsCounty, County.class);
            if( !( countyA.getName().equals(c.getName()) || countyB.getName().equals(c.getName()) ) ) {
                Assert.fail();
            }
        }
    }

    @Test
    public void test_RestServiceResult_query_NO_CONTENT() {

        when(mockedDao.load(County.class)).thenReturn(
                TestHelper.countyListFactory()
        );

        RestServiceResult res = restService.query(County.class);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NO_CONTENT, res.getHttpStatus());
    }
}
