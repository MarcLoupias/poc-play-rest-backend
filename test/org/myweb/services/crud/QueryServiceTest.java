package org.myweb.services.crud;

import com.fasterxml.jackson.databind.JsonNode;
import models.Category;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.Dao;
import org.myweb.db.TestHelper;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import play.libs.Json;
import play.mvc.Http;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueryServiceTest {
    private Dao mockedDao = mock(Dao.class);
    private Category categA;
    private Category categB;

    @Before
    public void setUp() throws Exception {

        categA = TestHelper.categoryFactory(1l, "categA");
        categB = TestHelper.categoryFactory(2l, "categB");

        when(mockedDao.loadAll(Category.class)).thenReturn(
                TestHelper.categoryListFactory(categA, categB)
        );
    }

    @Test
    public void test_JavaServiceResult_loadAll_OK() {

        JavaServiceResult res = QueryService.getInstance(mockedDao).loadAll(Category.class);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());

        @SuppressWarnings("unchecked")
        List<Category> categoryList = (List<Category>) res.getListContent();

        Assert.assertNotNull(categoryList);
        Assert.assertEquals(2, categoryList.size());

        for(Category category : categoryList) {
            if( !( categA.getName().equals(category.getName()) || categB.getName().equals(category.getName()) ) ) {
                Assert.fail();
            }
        }

    }

    @Test
    public void test_RestServiceResult_query_OK() {

        RestServiceResult res = QueryService.getInstance(mockedDao).query(Category.class);

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
    public void test_JavaServiceResult_loadAll_NO_CONTENT() {

        when(mockedDao.loadAll(Category.class)).thenReturn(
                TestHelper.categoryListFactory()
        );

        JavaServiceResult res = QueryService.getInstance(mockedDao).loadAll(Category.class);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NO_CONTENT, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_query_NO_CONTENT() {

        when(mockedDao.loadAll(Category.class)).thenReturn(
                TestHelper.categoryListFactory()
        );

        RestServiceResult res = QueryService.getInstance(mockedDao).query(Category.class);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NO_CONTENT, res.getHttpStatus());
    }
}
