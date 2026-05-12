package _bg.footballbettingapp.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
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

}
