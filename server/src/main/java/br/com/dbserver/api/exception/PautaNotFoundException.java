package br.com.dbserver.api.exception;

public class PautaNotFoundException extends RuntimeException {
    public PautaNotFoundException(String message) {
        super(message);
    }
} 