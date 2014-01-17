package org.myweb.services.crud.get;

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

public class GetServiceRestImplTest {
    private DaoJpa mockedDao = mock(DaoJpa.class);
    private Category categA;
    private GetServiceRestImpl restService;

    @Before
    public void setUp() throws Exception {

        categA = TestHelper.categoryFactory(1l, "categA");

        when(mockedDao.load(Category.class, 1l)).thenReturn(
                categA
        );

        GetServiceJavaImpl javaService = new GetServiceJavaImpl(mockedDao);
        restService = new GetServiceRestImpl(javaService);
    }

    @Test
    public void test_RestServiceResult_load_NOT_FOUND() {
        RestServiceResult res = restService.get(Category.class, 666l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_load_OK() {
        RestServiceResult res = restService.get(Category.class, categA.getId());

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());

        JsonNode jsCategory = res.getJsContent();
        Category c = Json.fromJson(jsCategory, Category.class);

        Assert.assertNotNull(c);
        Assert.assertEquals(c.getName(), categA.getName());
    }
}
