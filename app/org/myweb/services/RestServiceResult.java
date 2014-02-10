package org.myweb.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import play.i18n.Messages;
import play.mvc.Result;

import static play.mvc.Http.Status.*;
import static play.mvc.Results.*;

@SuppressWarnings("UnusedDeclaration")
public class RestServiceResult extends AbstractServiceResult {

    @Nullable
    private JsonNode jsContent = null;

    @Nullable
    public JsonNode getJsContent() {
        return jsContent;
    }

    private RestServiceResult() {

    }

    private RestServiceResult(int httpStatus) {
        super(httpStatus);
    }

    private RestServiceResult(int httpStatus, @Nullable String errorMsg, @Nullable String userMsg) {
        super(httpStatus, errorMsg, userMsg);
    }

    private RestServiceResult(@NotNull JavaServiceResult jsr) {
        super(jsr.httpStatus, jsr.errorMsg, jsr.userMsg);
    }

    private RestServiceResult(int httpStatus, @Nullable JsonNode jsContent) {
        super(httpStatus);
        this.jsContent = jsContent;
    }

    public static RestServiceResult buildServiceResult(int httpStatus) {
        RestServiceResult res = new RestServiceResult(httpStatus);
        res.logError();
        return res;
    }

    public static RestServiceResult buildServiceResult(
            int httpStatus, @Nullable String errorMsg, @Nullable String userMsg
    ) {
        RestServiceResult res = new RestServiceResult(httpStatus, errorMsg, userMsg);
        res.logError();
        return res;
    }

    public static RestServiceResult buildServiceResult(@NotNull JavaServiceResult jsr) {
        RestServiceResult res = new RestServiceResult(jsr);
        res.logError();
        return res;
    }

    public static RestServiceResult buildServiceResult(int httpStatus, @Nullable JsonNode jsContent) {
        RestServiceResult res = new RestServiceResult(httpStatus, jsContent);
        res.logError();
        return res;
    }

    public static RestServiceResult buildGenericServiceResultError(@NotNull Throwable t) {
        RestServiceResult res = new RestServiceResult(
                INTERNAL_SERVER_ERROR,
                ExceptionUtils.getStackTrace(t),
                Messages.get("rest.service.result.generic.error.msg")
        );
        res.logError();
        return res;
    }

    public Result buildPlayCtrlResult() {
        switch(this.httpStatus) {
            case OK: {
                if(this.jsContent != null) {
                    return ok(this.jsContent);
                }
                return ok();
            }
            case NO_CONTENT: {
                return noContent();
            }
            case CREATED: {
                return created(this.jsContent);
            }
            case BAD_REQUEST: {
                if(this.userMsg != null) {
                    return badRequest(this.userMsg);
                } else if (this.jsContent != null) {
                    return badRequest(this.jsContent);
                } else {
                    return badRequest();
                }

            }
            case UNAUTHORIZED: {
                return unauthorized(this.userMsg);
            }
            case NOT_FOUND: {
                return notFound(this.userMsg);
            }
            case INTERNAL_SERVER_ERROR: {
                return internalServerError(this.userMsg);
            }
            default: {
                return internalServerError();
            }
        }
    }
}
