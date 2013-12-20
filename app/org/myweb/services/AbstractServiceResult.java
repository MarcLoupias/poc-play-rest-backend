package org.myweb.services;

import org.jetbrains.annotations.Nullable;
import play.Logger;

import static play.mvc.Http.Status.*;


public abstract class AbstractServiceResult {

    protected int httpStatus;
    @Nullable
    protected String errorMsg;
    @Nullable
    protected String userMsg;

    public int getHttpStatus() {
        return httpStatus;
    }

    @Nullable
    public String getErrorMsg() {
        return errorMsg;
    }

    @Nullable
    public String getUserMsg() {
        return userMsg;
    }

    protected AbstractServiceResult() {
    }

    protected AbstractServiceResult(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    protected AbstractServiceResult(int httpStatus, @Nullable String errorMsg, @Nullable String userMsg) {
        this(httpStatus);
        this.errorMsg = errorMsg;
        this.userMsg = userMsg;
    }

    protected void logError() {
        switch(this.httpStatus) {
            case OK:
            case CREATED:
            case NO_CONTENT:
            case NOT_FOUND: {
                break;
            }

            default: {
                if(this.errorMsg == null || this.errorMsg.isEmpty()) {
                    this.errorMsg = "No error msg set for error " + this.httpStatus;
                }
                Logger.error("[ServiceError] httpStatus=" + this.httpStatus + " - " + this.errorMsg);
                break;
            }
        }

    }

}
