package org.myweb.services.crud.delete;

import models.Category;
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
    private Category categA;
    private DeleteServiceJavaImpl javaService;

    @Before
    public void setUp() throws Exception {

        categA = TestHelper.categoryFactory(1l, "categA");

        javaService = new DeleteServiceJavaImpl(mockedDao);
    }

    @Test
    public void test_JavaServiceResult_remove_NO_CONTENT() {
        JavaServiceResult res = javaService.remove(categA);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NO_CONTENT, res.getHttpStatus());
    }
}
