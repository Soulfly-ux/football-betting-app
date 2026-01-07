package _bg.footballbettingapp.exception;

import java.util.UUID;

public class MatchEditException extends DomainException {

   private final UUID matchId;

    public MatchEditException(UUID matchId,String message) {
        super(message);
        this.matchId = matchId;

    }

    public MatchEditException(String message, Throwable cause, UUID matchId) {
        super(message, cause);

        this.matchId = matchId;
    }

    public UUID getMatchId() {
        return matchId;
    }

}
