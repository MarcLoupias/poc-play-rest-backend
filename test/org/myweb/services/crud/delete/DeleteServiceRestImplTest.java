package org.myweb.services.crud.delete;

import models.Category;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.Dao;
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
    private Category categA;
    private DeleteServiceRestImpl restService;

    @Before
    public void setUp() throws Exception {

        categA = TestHelper.categoryFactory(1l, "categA");

        when(getServiceJava.get(Category.class, 1l)).thenReturn(
                JavaServiceResult.buildServiceResult(OK, categA)
        );

        DeleteServiceJavaImpl javaService = new DeleteServiceJavaImpl(mockedDao);
        restService = new DeleteServiceRestImpl(javaService, getServiceJava);
    }

    @Test
    public void test_RestServiceResult_delete_NO_CONTENT() {
        RestServiceResult res = restService.delete(Category.class, categA.getId());

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NO_CONTENT, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_delete_BAD_REQUEST_idMissing() {
        RestServiceResult res = restService.delete(Category.class, null);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());

        res = restService.delete(Category.class, 0l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }
}
