package com.cypoem.retrofit.bean;

import java.io.Serializable;

/**
 * Created by Dell on 2017/12/21.
 */

public class BaseBean<T> implements Serializable {
    private int code;
    private String message;
    private T results;
    private boolean error;

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
