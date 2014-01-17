package org.myweb.services.crud.update;

import models.Category;
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

    private Category categA;
    private UpdateServiceJavaImpl javaService;

    @Before
    public void setUp() throws Exception {

        categA = TestHelper.categoryFactory(1l, "categA");

        javaService = new UpdateServiceJavaImpl(mockedDao);
    }

    @Test
    public void test_JavaServiceResult_merge_OK() {
        categA.setName("categAAA");
        JavaServiceResult res = javaService.merge(categA);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
    }
}
