package org.myweb.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnusedDeclaration")
public class ServiceException extends Exception {

    @NotNull
    private String userMessage;

    private int httpStatus;

    private JsonNode serializedFormErrors;

    @NotNull
    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(@NotNull String userMessage) {
        this.userMessage = userMessage;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public JsonNode getSerializedFormErrors() {
        return serializedFormErrors;
    }

    public void setSerializedFormErrors(JsonNode serializedFormErrors) {
        this.serializedFormErrors = serializedFormErrors;
    }

    public ServiceException() {
        super();
    }

    public ServiceException(
            @NotNull String origin, int httpStatus, @NotNull String errorMessage,
            @NotNull String userMessage
    ) {
        super(computeExceptionMessage(origin, httpStatus, errorMessage));
        this.setUserMessage(userMessage);
        this.setHttpStatus(httpStatus);
    }

    public ServiceException(
            @NotNull String origin, int httpStatus, @NotNull String errorMessage,
            @NotNull String userMessage, @NotNull JsonNode serializedFormErrors
    ) {
        super(computeExceptionMessage(origin, httpStatus, errorMessage));
        this.setUserMessage(userMessage);
        this.setSerializedFormErrors(serializedFormErrors);
        this.setHttpStatus(httpStatus);
    }

    public ServiceException(
            @NotNull String origin, int httpStatus, @NotNull String errorMessage,
            @NotNull Throwable cause, @NotNull String userMessage
    ) {
        super(computeExceptionMessage(origin, httpStatus, errorMessage), cause);
        this.setUserMessage(userMessage);
        this.setHttpStatus(httpStatus);
    }

    public ServiceException(
            @NotNull String origin, int httpStatus, @NotNull String errorMessage,
            @NotNull Throwable cause,
            @NotNull String userMessage, @NotNull JsonNode serializedFormErrors
    ) {
        super(computeExceptionMessage(origin, httpStatus, errorMessage), cause);
        this.setHttpStatus(httpStatus);
    }

    private static String computeExceptionMessage(@NotNull String origin, int httpStatus, @NotNull String errorMessage) {
        return "[" + origin + "Error][httpStatus=" + httpStatus + "] - " + errorMessage;
    }
}
