package org.myweb.utils.rest;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class FilterParserServiceImplTest {

    @Test
    public void splitParameters_validRestFilter() {
        String validRestFilter = "name[like]lot|code[eq]4";

        FilterParserServiceImpl service = new FilterParserServiceImpl();

        String[] res = service.splitParameters(validRestFilter);

        Assert.assertNotNull(res);

        Assert.assertTrue("res.length should be equal to 2", (res.length == 2));
    }

    @Test
    public void splitParameters_invalidRestFilter() {
        String invalidRestFilter = "name[like]lot{code[eq]4";

        FilterParserServiceImpl service = new FilterParserServiceImpl();

        String[] res = service.splitParameters(invalidRestFilter);

        Assert.assertNotNull(res);

        Assert.assertTrue("res.length should be equal to 1", (res.length == 1));
    }

    @Test
    public void extractEntityPropertyName_validRestFilter() {
        String validRestFilter = "name[like]lot";

        FilterParserServiceImpl service = new FilterParserServiceImpl();

        String res = null;

        try {
            res = service.extractEntityPropertyName(validRestFilter);
        } catch (FilterParserServiceException e) {
            Assert.fail();
        }

        Assert.assertNotNull(res);
        Assert.assertEquals("res should equals name", "name", res);
    }

    @Test
    public void extractEntityPropertyName_invalidRestFilter() {
        String validRestFilter = "name_like]lot";

        FilterParserServiceImpl service = new FilterParserServiceImpl();

        String res = null;
        boolean exceptionThrown = false;

        try {
            res = service.extractEntityPropertyName(validRestFilter);
        } catch (FilterParserServiceException e) {
            Assert.assertNotNull(e);
            exceptionThrown = true;
        }

        Assert.assertTrue(exceptionThrown);
        Assert.assertNull(res);
    }

    @Test
    public void extractEntityPropertyValue_validRestFilter() {
        String validRestFilter = "name[like]lot";

        FilterParserServiceImpl service = new FilterParserServiceImpl();

        String res = null;

        try {
            res = service.extractEntityPropertyValue(validRestFilter);
        } catch (FilterParserServiceException e) {
            Assert.fail();
        }

        Assert.assertNotNull(res);
        Assert.assertEquals("res should equals lot", "lot", res);
    }

    @Test
    public void extractEntityPropertyValue_invalidRestFilter() {
        String invalidRestFilter = "name[like{lot";

        FilterParserServiceImpl service = new FilterParserServiceImpl();

        String res = null;
        boolean exceptionThrown = false;

        try {
            res = service.extractEntityPropertyValue(invalidRestFilter);
        } catch (FilterParserServiceException e) {
            Assert.assertNotNull(e);
            exceptionThrown = true;
        }

        Assert.assertTrue(exceptionThrown);
        Assert.assertNull(res);
    }

    @Test
    public void isLogicalOperatorValid() {

        FilterParserServiceImpl service = new FilterParserServiceImpl();

        boolean res;

        res = service.isLogicalOperatorValid(FilterParserServiceImpl.FILTER_REST_LOGICAL_OPERATOR_LIST[0]);
        Assert.assertEquals(FilterParserServiceImpl.FILTER_REST_LOGICAL_OPERATOR_LIST[0] + " should be valid", true, res);

        res = service.isLogicalOperatorValid(FilterParserServiceImpl.FILTER_REST_LOGICAL_OPERATOR_LIST[1]);
        Assert.assertEquals(FilterParserServiceImpl.FILTER_REST_LOGICAL_OPERATOR_LIST[1] + " should be valid", true, res);

        res = service.isLogicalOperatorValid(FilterParserServiceImpl.FILTER_REST_LOGICAL_OPERATOR_LIST[2]);
        Assert.assertEquals(FilterParserServiceImpl.FILTER_REST_LOGICAL_OPERATOR_LIST[2] + " should be valid", true, res);

        res = service.isLogicalOperatorValid(FilterParserServiceImpl.FILTER_REST_LOGICAL_OPERATOR_LIST[3]);
        Assert.assertEquals(FilterParserServiceImpl.FILTER_REST_LOGICAL_OPERATOR_LIST[3] + " should be valid", true, res);

        res = service.isLogicalOperatorValid(FilterParserServiceImpl.FILTER_REST_LOGICAL_OPERATOR_LIST[4]);
        Assert.assertEquals(FilterParserServiceImpl.FILTER_REST_LOGICAL_OPERATOR_LIST[4] + " should be valid", true, res);

        res = service.isLogicalOperatorValid(FilterParserServiceImpl.FILTER_REST_LOGICAL_OPERATOR_LIST[5]);
        Assert.assertEquals(FilterParserServiceImpl.FILTER_REST_LOGICAL_OPERATOR_LIST[5] + " should be valid", true, res);

        res = service.isLogicalOperatorValid(FilterParserServiceImpl.FILTER_REST_LOGICAL_OPERATOR_LIST[6]);
        Assert.assertEquals(FilterParserServiceImpl.FILTER_REST_LOGICAL_OPERATOR_LIST[6] + " should be valid", true, res);
    }

    @Test
    public void extractLogicalOperator_validRestFilter() {
        String validRestFilter = "name[like]lot";

        FilterParserServiceImpl service = new FilterParserServiceImpl();

        String res = null;

        try {
            res = service.extractLogicalOperator(validRestFilter);
        } catch (FilterParserServiceException e) {
            Assert.fail();
        }

        Assert.assertNotNull(res);
        Assert.assertEquals("res should equals like", "like", res);
    }

    @Test
    public void extractLogicalOperator_validRestFilterButInvalidLogicalOperator() {
        String validRestFilter = "name[==]lot";

        FilterParserServiceImpl service = new FilterParserServiceImpl();

        String res = null;
        boolean exceptionThrown = false;

        try {
            res = service.extractLogicalOperator(validRestFilter);
        } catch (FilterParserServiceException e) {
            Assert.assertNotNull(e);
            Assert.assertTrue(
                    "FilterParserServiceException message should start with Invalid logical operator",
                    ( e.getMessage().startsWith("Invalid logical operator") )
            );
            exceptionThrown = true;
        }

        Assert.assertTrue(exceptionThrown);
        Assert.assertNull(res);
    }

    @Test
    public void extractLogicalOperator_invalidRestFilter() {
        String invalidRestFilter = "name[like)lot";

        FilterParserServiceImpl service = new FilterParserServiceImpl();

        String res = null;
        boolean exceptionThrown = false;

        try {
            res = service.extractLogicalOperator(invalidRestFilter);
        } catch (FilterParserServiceException e) {
            Assert.assertNotNull(e);
            exceptionThrown = true;
        }

        Assert.assertTrue(exceptionThrown);
        Assert.assertNull(res);
    }

    @Test
    public void parse_validRestFilter() {
        String validRestFilter = "name[like]lot|code[eq]4";

        FilterParserServiceImpl service = new FilterParserServiceImpl();
        List<ImmutableTriple<String, String, String>> res = new ArrayList<>();

        try {
            res = service.parse(validRestFilter);
        } catch (FilterParserServiceException e) {
            Assert.fail();
        }

        Assert.assertNotNull(res);
        Assert.assertTrue("res.size() should be equal to 2", (res.size() == 2));

        ImmutableTriple<String, String, String> firstFilter = res.get(0);
        Assert.assertNotNull(firstFilter);
        Assert.assertNotNull(firstFilter.getLeft());
        Assert.assertEquals("firstFilter.getLeft() should equals name", "name", firstFilter.getLeft());
        Assert.assertNotNull(firstFilter.getMiddle());
        Assert.assertEquals("firstFilter.getMiddle() should equals like", "like", firstFilter.getMiddle());
        Assert.assertNotNull(firstFilter.getRight());
        Assert.assertEquals("firstFilter.getRight() should equals lot", "lot", firstFilter.getRight());

        ImmutableTriple<String, String, String> secondFilter = res.get(1);
        Assert.assertNotNull(secondFilter);
        Assert.assertNotNull(secondFilter.getLeft());
        Assert.assertEquals("secondFilter.getLeft() should equals code", "code", secondFilter.getLeft());
        Assert.assertNotNull(secondFilter.getMiddle());
        Assert.assertEquals("secondFilter.getMiddle() should equals eq", "eq", secondFilter.getMiddle());
        Assert.assertNotNull(secondFilter.getRight());
        Assert.assertEquals("secondFilter.getRight() should equals 4", "4", secondFilter.getRight());
    }

    @Test
    public void parse_invalidRestFilter() {
        String invalidRestFilter = "name{like]lot|code[eq4";

        FilterParserServiceImpl service = new FilterParserServiceImpl();
        List<ImmutableTriple<String, String, String>> res = new ArrayList<>();
        boolean exceptionThrown = false;

        try {
            res = service.parse(invalidRestFilter);
        } catch (FilterParserServiceException e) {
            Assert.assertNotNull(e);
            exceptionThrown = true;
        }

        Assert.assertTrue(exceptionThrown);
        Assert.assertTrue("res.size() should be equal to 0", (res.size() == 0));
    }
}
