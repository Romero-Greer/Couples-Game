package game.dao;
import java.util.List;
import game.model.Players;
import org.springframework.stereotype.Component;

public interface PlayersDao {
    List<Players> getPlayers();
    Players getPlayerByPlayerId(int playerId);
    Players addPlayer(Players player);
    List<Players> deletePlayer(int playerId);
    Players updatePlayer(Players player);
    Players getPlayerFromTeam(Players player);
}
