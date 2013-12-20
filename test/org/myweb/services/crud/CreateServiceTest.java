package org.myweb.services.crud;


import com.fasterxml.jackson.databind.JsonNode;
import models.Category;
import org.junit.Assert;
import org.junit.Test;
import org.myweb.db.Dao;
import org.myweb.db.TestHelper;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import play.libs.Json;
import play.mvc.Http;

import static org.mockito.Mockito.mock;

public class CreateServiceTest {
    private Dao mockedDao = mock(Dao.class);

    @Test
    public void test_JavaServiceResult_saveNew_CREATED() {
        Category newCategory = TestHelper.categoryFactory(null, "newCategory");
        JavaServiceResult res = CreateService.getInstance(mockedDao).saveNew(newCategory);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.CREATED, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_create_CREATED() {
        Category newCategory = TestHelper.categoryFactory(null, "newCategory");
        JsonNode jsNewCategory = Json.toJson(newCategory);
        RestServiceResult res = CreateService.getInstance(mockedDao).create(Category.class, jsNewCategory);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.CREATED, res.getHttpStatus());
    }
}
