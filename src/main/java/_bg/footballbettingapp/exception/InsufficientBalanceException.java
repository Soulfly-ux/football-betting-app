package _bg.footballbettingapp.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InsufficientBalanceException extends DomainException{

    private final UUID matchId;

    public InsufficientBalanceException(String message, UUID matchId) {
        super(message);
        this.matchId = matchId;
    }


}
