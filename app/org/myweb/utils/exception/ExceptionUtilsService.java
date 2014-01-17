package org.myweb.utils.exception;

import com.google.inject.ImplementedBy;

@ImplementedBy(ExceptionUtilsServiceImpl.class)
public interface ExceptionUtilsService {
    public String throwableToString(Throwable t);
}
