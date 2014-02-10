package org.myweb.services.crud.update;

import models.County;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.DaoJpa;
import org.myweb.services.JavaServiceResult;
import org.myweb.utils.test.TestHelper;
import play.mvc.Http;

import static org.mockito.Mockito.mock;

public class UpdateServiceJavaImplTest {
    private DaoJpa mockedDao = mock(DaoJpa.class);

    private County countyA;
    private UpdateServiceJavaImpl javaService;

    @Before
    public void setUp() throws Exception {

        countyA = TestHelper.countyFactory(1l, "46", "Lot");

        javaService = new UpdateServiceJavaImpl(mockedDao);
    }

    @Test
    public void test_JavaServiceResult_merge_OK() {
        countyA.setName("Lot en Quercy !");
        JavaServiceResult res = javaService.merge(countyA);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }
}
