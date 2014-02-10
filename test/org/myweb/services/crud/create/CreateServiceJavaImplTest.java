package org.myweb.services.crud.create;

import models.County;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.myweb.db.Dao;
import org.myweb.services.JavaServiceResult;
import org.myweb.utils.test.TestHelper;
import play.mvc.Http;

import static org.mockito.Mockito.mock;

public class CreateServiceJavaImplTest {
    private Dao mockedDao = mock(Dao.class);
    private CreateServiceJavaImpl javaService;


    @Before
    public void setUp() throws Exception {
        javaService = new CreateServiceJavaImpl(mockedDao);
    }

    @Test
    public void test_JavaServiceResult_saveNew_CREATED() {
        County newCounty = TestHelper.countyFactory(null, "46", "Lot");
        JavaServiceResult res = javaService.saveNew(newCounty);

        Assert.assertNotNull(res);
        Assert.assertEquals(Http.Status.CREATED, res.getHttpStatus());
    }
}
