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

public class UpdateServiceTest {
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
    public void test_JavaServiceResult_merge_OK() {
        categA.setName("categAAA");
        JavaServiceResult res = UpdateService.getInstance(mockedDao).merge(categA);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_update_OK() {
        categA.setName("categAAA");
        JsonNode jsCategory = Json.toJson(categA);

        RestServiceResult res = UpdateService.getInstance(mockedDao).update(Category.class, jsCategory, categA.getId());

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_update_BAD_REQUEST_idMissing() {

        categA.setName("categAAA");
        JsonNode jsCategory = Json.toJson(categA);
        RestServiceResult res = UpdateService.getInstance(mockedDao).update(Category.class, jsCategory, null);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());

        res = UpdateService.getInstance(mockedDao).update(Category.class, jsCategory, 0l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_update_BAD_REQUEST_urlIdAndPutResourceIdDiffer() {

        categA.setName("categAAA");
        JsonNode jsCategory = Json.toJson(categA);
        RestServiceResult res = UpdateService.getInstance(mockedDao).update(Category.class, jsCategory, 333l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }
}
