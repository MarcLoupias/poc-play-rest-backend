package org.myweb.services.crud.query;

import com.fasterxml.jackson.databind.JsonNode;
import models.Category;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.DaoJpa;
import org.myweb.utils.test.TestHelper;
import org.myweb.services.RestServiceResult;
import play.libs.Json;
import play.mvc.Http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueryServiceRestImplTest {
    private DaoJpa mockedDao = mock(DaoJpa.class);
    private Category categA;
    private Category categB;
    private QueryServiceRestImpl restService;

    @Before
    public void setUp() throws Exception {

        categA = TestHelper.categoryFactory(1l, "categA");
        categB = TestHelper.categoryFactory(2l, "categB");

        when(mockedDao.loadAll(Category.class)).thenReturn(
                TestHelper.categoryListFactory(categA, categB)
        );

        QueryServiceJavaImpl javaService = new QueryServiceJavaImpl(mockedDao);
        restService = new QueryServiceRestImpl(javaService);
    }

    @Test
    public void test_RestServiceResult_query_OK() {
        RestServiceResult res = restService.query(Category.class);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());

        JsonNode jsCategoryList = res.getJsContent();
        Assert.assertNotNull(jsCategoryList);
        Assert.assertTrue(jsCategoryList.isArray());
        Assert.assertEquals(2, jsCategoryList.size());

        for(JsonNode jsCategory : jsCategoryList) {
            Category c = Json.fromJson(jsCategory, Category.class);
            if( !( categA.getName().equals(c.getName()) || categB.getName().equals(c.getName()) ) ) {
                Assert.fail();
            }
        }
    }

    @Test
    public void test_RestServiceResult_query_NO_CONTENT() {

        when(mockedDao.loadAll(Category.class)).thenReturn(
                TestHelper.categoryListFactory()
        );

        RestServiceResult res = restService.query(Category.class);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NO_CONTENT, res.getHttpStatus());
    }
}
