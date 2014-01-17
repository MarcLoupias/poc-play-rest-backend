package org.myweb.services.crud.update;

import com.fasterxml.jackson.databind.JsonNode;
import models.Category;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.DaoJpa;
import org.myweb.utils.test.TestHelper;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.crud.get.GetServiceJava;
import play.libs.Json;
import play.mvc.Http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Status.OK;

public class UpdateServiceRestImplTest {
    private DaoJpa mockedDao = mock(DaoJpa.class);
    private GetServiceJava getServiceJava = mock(GetServiceJava.class);
    private Category categA;
    private UpdateServiceRestImpl restService;

    @Before
    public void setUp() throws Exception {

        categA = TestHelper.categoryFactory(1l, "categA");

        when(getServiceJava.get(Category.class, 1l)).thenReturn(
                JavaServiceResult.buildServiceResult(OK, categA)
        );

        UpdateServiceJavaImpl javaService = new UpdateServiceJavaImpl(mockedDao);
        restService = new UpdateServiceRestImpl(javaService, getServiceJava);
    }

    @Test
    public void test_RestServiceResult_update_OK() {

        categA.setName("categAAA");
        JsonNode jsCategory = Json.toJson(categA);

        RestServiceResult res = restService.update(Category.class, jsCategory, categA.getId());

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_update_BAD_REQUEST_idMissing() {

        categA.setName("categAAA");
        JsonNode jsCategory = Json.toJson(categA);
        RestServiceResult res = restService.update(Category.class, jsCategory, null);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());

        res = restService.update(Category.class, jsCategory, 0l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_update_BAD_REQUEST_urlIdAndPutResourceIdDiffer() {

        categA.setName("categAAA");
        JsonNode jsCategory = Json.toJson(categA);
        RestServiceResult res = restService.update(Category.class, jsCategory, 333l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }
}
