package org.myweb.services.crud.delete;

import models.County;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.DaoJpa;
import org.myweb.services.JavaServiceResult;
import org.myweb.utils.test.TestHelper;
import play.mvc.Http;

import static org.mockito.Mockito.mock;

public class DeleteServiceJavaImplTest {
    private DaoJpa mockedDao = mock(DaoJpa.class);
    private County countyA;
    private DeleteServiceJavaImpl javaService;

    @Before
    public void setUp() throws Exception {

        countyA = TestHelper.countyFactory(1l, "46", "Lot");

        javaService = new DeleteServiceJavaImpl(mockedDao);
    }

    @Test
    public void test_JavaServiceResult_remove_NO_CONTENT() {
        JavaServiceResult res = javaService.remove(countyA);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NO_CONTENT, res.getHttpStatus());
    }
}
