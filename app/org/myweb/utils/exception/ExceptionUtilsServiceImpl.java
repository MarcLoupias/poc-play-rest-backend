package org.myweb.utils.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtilsServiceImpl implements ExceptionUtilsService {
    public String throwableToString(Throwable t) {
        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    public static String _throwableToString(Throwable t) {
        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
