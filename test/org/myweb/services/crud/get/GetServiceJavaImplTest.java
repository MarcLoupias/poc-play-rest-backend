package org.myweb.services.crud.get;

import models.County;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.DaoJpa;
import org.myweb.services.JavaServiceResult;
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

        javaService = new GetServiceJavaImpl(mockedDao, filterParserService);
    }

    @Test
    public void test_JavaServiceResult_load_NOT_FOUND() {
        JavaServiceResult res = javaService.get(County.class, 666l);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    public void test_JavaServiceResult_load_OK() {
        JavaServiceResult res = javaService.get(County.class, countyA.getId());

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());

        County c = (County) res.getSingleContent();

        Assert.assertNotNull(c);
        Assert.assertEquals(c.getName(), countyA.getName());
    }
}
