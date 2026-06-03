package game.dao;

import game.model.Scores;
import game.model.Teams;

import java.util.List;

public interface ScoresDao {
    void initializeScores(List<Teams> teams);
    List<Scores> getScores();
    Scores addPointToTeam(int teamId);
    void resetAllScores();
    void deleteAllScores();
}
