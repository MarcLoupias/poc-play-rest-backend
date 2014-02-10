package org.myweb.services.crud.delete;

import models.County;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.Dao;
import org.myweb.services.ServiceException;
import org.myweb.utils.test.TestHelper;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.crud.get.GetServiceJava;
import play.mvc.Http;

import static play.mvc.Http.Status.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeleteServiceRestImplTest {
    private Dao mockedDao = mock(Dao.class);
    private GetServiceJava getServiceJava = mock(GetServiceJava.class);
    private County countyA;
    private DeleteServiceRestImpl restService;

    @Before
    public void setUp() throws Exception {

        countyA = TestHelper.countyFactory(1l, "46", "Lot");

        when(getServiceJava.get(County.class, 1l)).thenReturn(
                JavaServiceResult.buildServiceResult(OK, countyA)
        );

        DeleteServiceJavaImpl javaService = new DeleteServiceJavaImpl(mockedDao);
        restService = new DeleteServiceRestImpl(javaService, getServiceJava);
    }

    @Test
    public void test_RestServiceResult_delete_NO_CONTENT() {
        RestServiceResult res = null;
        try {
            res = restService.delete(County.class, countyA.getId());
        } catch (ServiceException e) {
            Assert.fail();
        }

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NO_CONTENT, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_delete_BAD_REQUEST_idMissing() {

        try {
            restService.delete(County.class, null);
        } catch (ServiceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(Http.Status.BAD_REQUEST, e.getHttpStatus());
        }

        try {
            restService.delete(County.class, 0l);
        } catch (ServiceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(Http.Status.BAD_REQUEST, e.getHttpStatus());
        }
    }
}
