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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetServiceTest {
    private Dao mockedDao = mock(Dao.class);
    private Category categA;

    @Before
    public void setUp() throws Exception {

        categA = TestHelper.categoryFactory(1l, "categA");

        when(mockedDao.load(Category.class, 1l)).thenReturn(
                categA
        );
    }

    @Test
    public void test_JavaServiceResult_load_NOT_FOUND() {

        JavaServiceResult res = GetService.getInstance(mockedDao).load(Category.class, 666l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_load_NOT_FOUND() {

        RestServiceResult res = GetService.getInstance(mockedDao).get(Category.class, 666l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    public void test_JavaServiceResult_load_OK() {

        JavaServiceResult res = GetService.getInstance(mockedDao).load(Category.class, categA.getId());

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());

        Category c = (Category) res.getSingleContent();

        Assert.assertNotNull(c);
        Assert.assertEquals(c.getName(), categA.getName());
    }

    @Test
    public void test_RestServiceResult_load_OK() {

        RestServiceResult res = GetService.getInstance(mockedDao).get(Category.class, categA.getId());

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());

        JsonNode jsCategory = res.getJsContent();
        Category c = Json.fromJson(jsCategory, Category.class);

        Assert.assertNotNull(c);
        Assert.assertEquals(c.getName(), categA.getName());
    }
}
