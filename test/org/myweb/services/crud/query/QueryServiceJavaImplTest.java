package org.myweb.services.crud.query;

import models.Category;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.DaoJpa;
import org.myweb.services.JavaServiceResult;
import org.myweb.utils.test.TestHelper;
import play.mvc.Http;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueryServiceJavaImplTest {
    private DaoJpa mockedDao = mock(DaoJpa.class);
    private Category categA;
    private Category categB;
    private QueryServiceJavaImpl javaService;

    @Before
    public void setUp() throws Exception {

        categA = TestHelper.categoryFactory(1l, "categA");
        categB = TestHelper.categoryFactory(2l, "categB");

        when(mockedDao.loadAll(Category.class)).thenReturn(
                TestHelper.categoryListFactory(categA, categB)
        );

        javaService = new QueryServiceJavaImpl(mockedDao);
    }

    @Test
    public void test_JavaServiceResult_loadAll_OK() {
        JavaServiceResult res = javaService.loadAll(Category.class);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());

        @SuppressWarnings("unchecked")
        List<Category> categoryList = (List<Category>) res.getListContent();

        Assert.assertNotNull(categoryList);
        Assert.assertEquals(2, categoryList.size());

        for(Category category : categoryList) {
            if( !( categA.getName().equals(category.getName()) || categB.getName().equals(category.getName()) ) ) {
                Assert.fail();
            }
        }
    }

    @Test
    public void test_JavaServiceResult_loadAll_NO_CONTENT() {

        when(mockedDao.loadAll(Category.class)).thenReturn(
                TestHelper.categoryListFactory()
        );

        JavaServiceResult res = javaService.loadAll(Category.class);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NO_CONTENT, res.getHttpStatus());
    }
}
