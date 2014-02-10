package org.myweb.services.crud.query;

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

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueryServiceJavaImplTest {
    private DaoJpa mockedDao = mock(DaoJpa.class);
    private County countyA, countyB;
    private QueryServiceJavaImpl javaService;
    private FilterParserService filterParserService = new FilterParserServiceImpl();

    @Before
    public void setUp() throws Exception {

        countyA = TestHelper.countyFactory(1l, "46", "Lot");
        countyB = TestHelper.countyFactory(2l, "47", "Lot-et-Garonne");

        when(mockedDao.load(County.class)).thenReturn(
                TestHelper.countyListFactory(countyA, countyB)
        );

        javaService = new QueryServiceJavaImpl(mockedDao, filterParserService);
    }

    @Test
    public void test_JavaServiceResult_loadAll_OK() {
        JavaServiceResult res = javaService.load(County.class);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.OK, res.getHttpStatus());

        @SuppressWarnings("unchecked")
        List<County> countyList = (List<County>) res.getListContent();

        Assert.assertNotNull(countyList);
        Assert.assertEquals(2, countyList.size());

        for(County county : countyList) {
            if( !( countyA.getName().equals(county.getName()) || countyB.getName().equals(county.getName()) ) ) {
                Assert.fail();
            }
        }
    }

    @Test
    public void test_JavaServiceResult_loadAll_NO_CONTENT() {

        when(mockedDao.load(County.class)).thenReturn(
                TestHelper.countyListFactory()
        );

        JavaServiceResult res = javaService.load(County.class);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.NO_CONTENT, res.getHttpStatus());
    }
}
