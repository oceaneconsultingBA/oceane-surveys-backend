package com.oceane.surveys.dto;

public class ApiResponse<T> {
    private T data;
    private String error;

    // Constructeur pour une réponse avec des données
    public ApiResponse(T data) {
        this.data = data;
    }

    // Constructeur pour une réponse avec un message d'erreur
    public ApiResponse(String error) {
        this.error = error;
    }

    // Getters / Setters
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
