package com.dgeorgiev.rx.http.client;

/**
 * Dimitar Georgiev, Novarto Ltd.
 * 25.12.14.
 */
public class HttpError {
    public final int code;
    public final String status;
    public final String bodyRaw;

    public HttpError(int code, String status, String bodyRaw) {
        this.code = code;
        this.status = status;
        this.bodyRaw = bodyRaw;
    }

    @Override
    public String toString() {
        return "HttpError{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", bodyRaw='" + bodyRaw + '\'' +
                '}';
    }
}
