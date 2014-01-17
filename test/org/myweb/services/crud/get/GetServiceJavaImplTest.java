package org.myweb.services.crud.get;

import models.Category;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.DaoJpa;
import org.myweb.services.JavaServiceResult;
import org.myweb.utils.test.TestHelper;
import play.mvc.Http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetServiceJavaImplTest {
    private DaoJpa mockedDao = mock(DaoJpa.class);
    private Category categA;
    private GetServiceJavaImpl javaService;

    @Before
    public void setUp() throws Exception {

        categA = TestHelper.categoryFactory(1l, "categA");

        when(mockedDao.load(Category.class, 1l)).thenReturn(
                categA
        );

        javaService = new GetServiceJavaImpl(mockedDao);
    }

    @Test
    public void test_JavaServiceResult_load_NOT_FOUND() {
        JavaServiceResult res = javaService.get(Category.class, 666l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    public void test_JavaServiceResult_load_OK() {
        JavaServiceResult res = javaService.get(Category.class, categA.getId());

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());

        Category c = (Category) res.getSingleContent();

        Assert.assertNotNull(c);
        Assert.assertEquals(c.getName(), categA.getName());
    }
}
