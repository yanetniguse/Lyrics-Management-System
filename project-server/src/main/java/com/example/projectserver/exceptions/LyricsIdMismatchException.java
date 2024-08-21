package com.example.projectserver.exceptions;

public class LyricsIdMismatchException extends RuntimeException{

    public LyricsIdMismatchException() {
        super();
    }

    public LyricsIdMismatchException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public LyricsIdMismatchException(final String message) {
        super(message);
    }

    public LyricsIdMismatchException(final Throwable cause) {
        super(cause);
    }
}
