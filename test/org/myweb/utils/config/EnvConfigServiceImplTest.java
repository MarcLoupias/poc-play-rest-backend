package org.myweb.utils.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnvConfigServiceImplTest {

    private static String ENV_VAR_STRING_IS_SET = "ENV_VAR_STRING_IS_SET";
    private static String ENV_VAR_STRING_IS_SET_STRING_VALUE = "robert";
    private static String ENV_VAR_STRING_NOT_SET = "ENV_VAR_STRING_NOT_SET";
    private static String ENV_VAR_STRING_IS_SET_THROWING_SECURITY_EXCEPTION =
            "ENV_VAR_STRING_IS_SET_THROWING_SECURITY_EXCEPTION";

    private static String ENV_VAR_LONG_IS_SET = "ENV_VAR_LONG_IS_SET";
    private static String ENV_VAR_LONG_IS_SET_STRING_VALUE = "10";
    private static String ENV_VAR_LONG_NOT_SET = "ENV_VAR_LONG_NOT_SET";
    private static String ENV_VAR_LONG_IS_SET_THROWING_NUMBER_FORMAT_EXCEPTION =
            "ENV_VAR_LONG_IS_SET_THROWING_NUMBER_FORMAT_EXCEPTION";


    private SystemGetEnvWrapper systemGetEnvWrapper = mock(SystemGetEnvWrapper.class);

    private EnvConfigServiceImpl service;

    @Before
    public void setUp() throws Exception {

        when(systemGetEnvWrapper.getEnv(ENV_VAR_STRING_IS_SET)).thenReturn(
                ENV_VAR_STRING_IS_SET_STRING_VALUE
        );
        when(systemGetEnvWrapper.getEnv(ENV_VAR_STRING_NOT_SET)).thenReturn(
                null
        );
        when(systemGetEnvWrapper.getEnv(ENV_VAR_STRING_IS_SET_THROWING_SECURITY_EXCEPTION)).thenThrow(
                new SecurityException("exception msg")
        );

        when(systemGetEnvWrapper.getEnv(ENV_VAR_LONG_IS_SET)).thenReturn(
                ENV_VAR_LONG_IS_SET_STRING_VALUE
        );
        when(systemGetEnvWrapper.getEnv(ENV_VAR_LONG_NOT_SET)).thenReturn(
                null
        );
        String ENV_VAR_LONG_IS_SET_THROWING_NUMBER_FORMAT_EXCEPTION_VALUE = "azerty";
        when(systemGetEnvWrapper.getEnv(ENV_VAR_LONG_IS_SET_THROWING_NUMBER_FORMAT_EXCEPTION)).thenReturn(
                ENV_VAR_LONG_IS_SET_THROWING_NUMBER_FORMAT_EXCEPTION_VALUE
        );

        service = new EnvConfigServiceImpl(systemGetEnvWrapper);
    }

    @Test
    public void getEnvVarAsString_ValIsSet() {
        String res = null;

        try {
            res = service.getEnvVarAsString(ENV_VAR_STRING_IS_SET);
        } catch (EnvConfigServiceException e) {
            Assert.fail("should not throw an exception");
        }

        Assert.assertNotNull("should not be null", res);
        Assert.assertEquals(
                "should equals " + ENV_VAR_STRING_IS_SET_STRING_VALUE, ENV_VAR_STRING_IS_SET_STRING_VALUE, res
        );
    }

    @Test
    public void getEnvVarAsString_ValIsNotSet() {
        String res = null;

        try {
            res = service.getEnvVarAsString(ENV_VAR_STRING_NOT_SET);
        } catch (EnvConfigServiceException e) {
            Assert.fail("should not throw an exception");
        }

        Assert.assertNull("should be null", res);
    }

    @Test
    public void getEnvVarAsString_ThrowingSecurityException() {
        boolean exception = false;
        try {
            service.getEnvVarAsString(ENV_VAR_STRING_IS_SET_THROWING_SECURITY_EXCEPTION);
        } catch (EnvConfigServiceException e) {
            exception = true;
            Assert.assertNotNull("should not be null", e);
            Assert.assertTrue(
                    "cause from exception thrown should be an instance of SecurityException",
                    ( e.getCause() instanceof SecurityException )
            );
        }

        Assert.assertTrue("exception should be true", exception);
    }

    @Test
    public void getEnvVarAsLong_ValIsSet() {
        Long res = null;

        try {
            res = service.getEnvVarAsLong(ENV_VAR_LONG_IS_SET);
        } catch (EnvConfigServiceException e) {
            Assert.fail("should not throw an exception");
        }

        Assert.assertNotNull("should not be null", res);
        Assert.assertEquals(
                "should equals " + ENV_VAR_LONG_IS_SET_STRING_VALUE, Long.parseLong(ENV_VAR_LONG_IS_SET_STRING_VALUE),
                res.longValue()
        );
    }

    @Test
    public void getEnvVarAsLong_ValIsNotSet() {
        Long res = null;

        try {
            res = service.getEnvVarAsLong(ENV_VAR_LONG_NOT_SET);
        } catch (EnvConfigServiceException e) {
            Assert.fail("should not throw an exception");
        }

        Assert.assertNull("should be null", res);
    }

    @Test
    public void getEnvVarAsLong_ThrowingNumberFormatException() {
        boolean exception = false;
        try {
            service.getEnvVarAsLong(ENV_VAR_LONG_IS_SET_THROWING_NUMBER_FORMAT_EXCEPTION);
        } catch (EnvConfigServiceException e) {
            exception = true;
            Assert.assertNotNull("should not be null", e);
            Assert.assertTrue(
                    "cause from exception thrown should be an instance of NumberFormatException",
                    ( e.getCause() instanceof NumberFormatException )
            );
        }

        Assert.assertTrue("exception should be true", exception);
    }
}
