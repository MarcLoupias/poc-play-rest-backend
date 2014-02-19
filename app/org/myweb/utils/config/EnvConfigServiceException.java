package org.myweb.utils.config;

@SuppressWarnings("UnusedDeclaration")
public class EnvConfigServiceException extends Exception {
    public EnvConfigServiceException() {
        super();
    }
    public EnvConfigServiceException(String message) {
        super(message);
    }
    public EnvConfigServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public EnvConfigServiceException(Throwable cause) {
        super(cause);
    }
}
