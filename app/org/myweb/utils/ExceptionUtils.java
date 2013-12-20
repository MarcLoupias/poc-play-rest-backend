package org.myweb.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
    public static String throwableToString(Throwable t) {
        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
