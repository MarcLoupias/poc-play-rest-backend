package org.myweb.services.crud.update;

import com.fasterxml.jackson.databind.JsonNode;
import models.County;
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
    private County countyA;
    private UpdateServiceRestImpl restService;

    @Before
    public void setUp() throws Exception {

        countyA = TestHelper.countyFactory(1l, "46", "Lot");

        when(getServiceJava.get(County.class, 1l)).thenReturn(
                JavaServiceResult.buildServiceResult(OK, countyA)
        );

        UpdateServiceJavaImpl javaService = new UpdateServiceJavaImpl(mockedDao);
        restService = new UpdateServiceRestImpl(javaService, getServiceJava);
    }

    @Test
    public void test_RestServiceResult_update_OK() {

        countyA.setName("Lot en Quercy !");
        JsonNode jsCounty = Json.toJson(countyA);

        RestServiceResult res = restService.update(County.class, jsCounty, countyA.getId());

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_update_BAD_REQUEST_idMissing() {

        countyA.setName("Lot en Quercy !");
        JsonNode jsCounty = Json.toJson(countyA);
        RestServiceResult res = restService.update(County.class, jsCounty, null);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());

        res = restService.update(County.class, jsCounty, 0l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_update_BAD_REQUEST_urlIdAndPutResourceIdDiffer() {

        countyA.setName("Lot en Quercy !");
        JsonNode jsCounty = Json.toJson(countyA);
        RestServiceResult res = restService.update(County.class, jsCounty, 333l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }
}
