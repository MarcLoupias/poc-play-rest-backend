package org.myweb.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import play.mvc.Result;

import static play.mvc.Http.Status.*;
import static play.mvc.Results.*;

public class RestServiceResult extends AbstractServiceResult {

    @Nullable
    private JsonNode jsContent = null;

    @Nullable
    public JsonNode getJsContent() {
        return jsContent;
    }

    @SuppressWarnings("UnusedDeclaration")
    private RestServiceResult() {

    }

    private RestServiceResult(int httpStatus) {
        super(httpStatus);
    }

    private RestServiceResult(@NotNull JavaServiceResult jsr) {
        super(jsr.httpStatus);
    }

    private RestServiceResult(int httpStatus, @Nullable JsonNode jsContent) {
        super(httpStatus);
        this.jsContent = jsContent;
    }

    public static RestServiceResult buildServiceResult(int httpStatus) {
        return new RestServiceResult(httpStatus);
    }

    public static RestServiceResult buildServiceResult(@NotNull JavaServiceResult jsr) {
        return new RestServiceResult(jsr);
    }

    public static RestServiceResult buildServiceResult(int httpStatus, @Nullable JsonNode jsContent) {
        return new RestServiceResult(httpStatus, jsContent);
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
            default: {
                return internalServerError();
            }
        }
    }
}
