package game.dao;

import game.model.Scores;
import game.model.Teams;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcScoresDao implements ScoresDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcScoresDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Scores mapScoresToRow(SqlRowSet rs) {
        Scores scores = new Scores();
        scores.setScoreId(rs.getInt("score_id"));
        scores.setTeamId(rs.getInt("team_id"));
        scores.setTeamName(rs.getString("team_name"));
        scores.setScore(rs.getInt("score"));
        return scores;
    }

    @Override
    public void initializeScores(List<Teams> teams) {
        String sql = "INSERT INTO scores (team_id, score) VALUES (?, 0) " +
                     "ON CONFLICT (team_id) DO UPDATE SET score = 0";
        for (Teams team : teams) {
            jdbcTemplate.update(sql, team.getTeamId());
        }
    }

    @Override
    public List<Scores> getScores() {
        List<Scores> scores = new ArrayList<>();
        String sql = "SELECT s.score_id, s.team_id, t.team_name, s.score " +
                     "FROM scores s JOIN teams t ON s.team_id = t.team_id " +
                     "ORDER BY s.score DESC";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            scores.add(mapScoresToRow(results));
        }
        return scores;
    }

    @Override
    public Scores addPointToTeam(int teamId) {
        String sql = "INSERT INTO scores (team_id, score) VALUES (?, 1) " +
                     "ON CONFLICT (team_id) DO UPDATE SET score = scores.score + 1 " +
                     "RETURNING score_id";
        SqlRowSet returning = jdbcTemplate.queryForRowSet(sql, teamId);
        if (!returning.next()) {
            return null;
        }
        int scoreId = returning.getInt("score_id");
        String selectSql = "SELECT s.score_id, s.team_id, t.team_name, s.score " +
                           "FROM scores s JOIN teams t ON s.team_id = t.team_id " +
                           "WHERE s.score_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(selectSql, scoreId);
        if (results.next()) {
            return mapScoresToRow(results);
        }
        return null;
    }

    @Override
    public void resetAllScores() {
        String sql = "UPDATE scores SET score = 0";
        jdbcTemplate.update(sql);
    }

    @Override
    public void deleteAllScores() {
        String sql = "DELETE FROM scores";
        jdbcTemplate.update(sql);
    }
}
