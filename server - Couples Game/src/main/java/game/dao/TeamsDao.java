package game.dao;
import java.util.List;
import game.model.Teams;
import org.springframework.stereotype.Component;

public interface TeamsDao {
    List<Teams> getTeams();
    Teams getTeamByTeamId(int teamId);
    Teams addTeam(Teams team);
    List<Teams> removeTeam(int teamId);
    Teams updateTeam(Teams team);
}
