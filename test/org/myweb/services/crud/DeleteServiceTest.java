package org.myweb.services.crud;

import models.Category;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.Dao;
import org.myweb.db.TestHelper;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import play.mvc.Http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeleteServiceTest {
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
    public void test_JavaServiceResult_remove_NO_CONTENT() {
        JavaServiceResult res = DeleteService.getInstance(mockedDao).remove(categA);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NO_CONTENT, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_delete_NO_CONTENT() {
        RestServiceResult res = DeleteService.getInstance(mockedDao).delete(Category.class, categA.getId());

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NO_CONTENT, res.getHttpStatus());
    }

    @Test
    public void test_RestServiceResult_delete_BAD_REQUEST_idMissing() {

        RestServiceResult res = DeleteService.getInstance(mockedDao).delete(Category.class, null);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());

        res = DeleteService.getInstance(mockedDao).delete(Category.class, 0l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.BAD_REQUEST, res.getHttpStatus());
    }
}
