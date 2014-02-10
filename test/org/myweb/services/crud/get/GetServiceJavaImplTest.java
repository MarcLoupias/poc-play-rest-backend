package org.myweb.services.crud.get;

import models.County;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.DaoJpa;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.ServiceException;
import org.myweb.utils.rest.FilterParserService;
import org.myweb.utils.rest.FilterParserServiceImpl;
import org.myweb.utils.test.TestHelper;
import play.mvc.Http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetServiceJavaImplTest {
    private DaoJpa mockedDao = mock(DaoJpa.class);
    private County countyA;
    private GetServiceJavaImpl javaService;
    private FilterParserService filterParserService = new FilterParserServiceImpl();

    @Before
    public void setUp() throws Exception {

        countyA = TestHelper.countyFactory(1l, "46", "Lot");

        when(mockedDao.load(County.class, 1l)).thenReturn(
                countyA
        );

        when(mockedDao.count(County.class, null)).thenReturn(
                1
        );

        javaService = new GetServiceJavaImpl(mockedDao, filterParserService);
    }

    @Test
    public void test_JavaServiceResult_get_NOT_FOUND() {
        boolean exception = false;
        try {
            javaService.get(County.class, 666l);
        } catch (ServiceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(Http.Status.NOT_FOUND, e.getHttpStatus());
            exception = true;
        }

        Assert.assertTrue("exception should be true", exception);
    }

    @Test
    public void test_JavaServiceResult_get_OK() {
        JavaServiceResult res = null;
        try {
            res = javaService.get(County.class, countyA.getId());
        } catch (ServiceException e) {
            Assert.fail();
        }

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());

        County c = (County) res.getSingleContent();

        Assert.assertNotNull(c);
        Assert.assertEquals(c.getName(), countyA.getName());
    }

    @Test
    public void test_JavaServiceResult_count_BAD_REQUEST() {
        boolean exception = false;
        try {
            javaService.count(County.class, "name[like]]toto");
        } catch (ServiceException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals(Http.Status.BAD_REQUEST, e.getHttpStatus());
            exception = true;
        }

        Assert.assertTrue("exception should be true", exception);
    }

    @Test
    public void test_JavaServiceResult_count_OK() {
        JavaServiceResult res = null;
        try {
            res = javaService.count(County.class);
        } catch (ServiceException e) {
            Assert.fail();
        }

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());
        Assert.assertEquals("count should equals to 1", 1, res.getCount());
    }
}
