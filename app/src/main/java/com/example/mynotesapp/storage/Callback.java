package com.example.mynotesapp.storage;

public interface Callback<T> {
    void onSuccess(T result);
    void onError(Throwable error);
}
