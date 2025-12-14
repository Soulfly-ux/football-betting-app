package _bg.footballbettingapp.exception;

import java.util.UUID;

public class InsufficientBalanceException extends DomainException{

    private final UUID matchId;

    public InsufficientBalanceException(String message, UUID matchId) {
        super(message);
        this.matchId = matchId;
    }


    public UUID getMatchId() {
        return matchId;
    }
}
