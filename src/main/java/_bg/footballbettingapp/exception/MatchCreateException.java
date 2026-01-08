package _bg.footballbettingapp.exception;

public class MatchCreateException extends DomainException{
    public MatchCreateException(String message) {
        super(message);
    }

    public MatchCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
