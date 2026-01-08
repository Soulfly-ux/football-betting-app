package _bg.footballbettingapp.exception;

import java.util.UUID;

public class MatchCancelException extends DomainException{

     private final UUID matchId;

    public MatchCancelException(UUID matchId, String message) {
        super(message);
        this.matchId = matchId;
    }

    public MatchCancelException(String message, Throwable cause, UUID matchId) {
        super(message, cause);
        this.matchId = matchId;
    }

    public UUID getMatchId() {
        return matchId;
    }
}
