package org.myweb.services;

public abstract class AbstractServiceResult {

    protected int httpStatus;

    public int getHttpStatus() {
        return httpStatus;
    }

    protected AbstractServiceResult() {
    }

    protected AbstractServiceResult(int httpStatus) {
        this.httpStatus = httpStatus;
    }
}
