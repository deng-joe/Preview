package com.joe.preview.data.remote.resources;

public class Resource<T> {

    private Status status;
    public T data;
    private String message;

    private Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String message, T data) {
        return new Resource<>(Status.ERROR, data, message);
    }

    public static <T> Resource<T> loading(T data) {
        return new Resource<>(Status.LOADING, data, null);
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS && data != null;
    }

    public boolean isLoading() {
        return status == Status.LOADING;
    }

    public boolean isLoaded() {
        return status != Status.LOADING;
    }
}
