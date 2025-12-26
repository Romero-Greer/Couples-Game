package game.dao;

import game.model.Teams;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTeamsDao implements TeamsDao {
    private JdbcTemplate jdbcTemplate;
    public JdbcTeamsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private Teams mapTeamsToRow(SqlRowSet rs) {
        Teams team = new Teams();
        team.setTeamId(rs.getInt("team_id"));
        team.setTeamName(rs.getString("team_name"));
        return team;
    }

    @Override
    public List<Teams> getTeams() {
        List<Teams> teams = new ArrayList<>();
        String sql = "SELECT * FROM teams";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        while (results.next()) {
            teams.add(mapTeamsToRow(results));
        }
        return teams;
    }

    @Override
    public Teams getTeamByTeamId(int teamId) {
        Teams team = null;
        String sql = "SELECT * FROM teams WHERE team_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, teamId);

        if (results.next()) {
            team = mapTeamsToRow(results);
        }
        return team;
    }

    @Override
    public Teams addTeam(Teams team) {
        String sql = "INSERT INTO  teams (team_name) VALUES (?) RETURNING team_id";

        int newTeamId = jdbcTemplate.queryForObject(sql, int.class, team.getTeamName());

        return getTeamByTeamId(newTeamId);
    }

    @Override
    public List<Teams> removeTeam(int teamId) {
        List<Teams> team = new ArrayList<>();
        String sql = "DELETE FROM teams WHERE team_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, teamId);
        if (rowsAffected > 0) {
            return getTeams();
        }
        System.out.println("Unable to remove team.");
        return getTeams();
    }

    @Override
    public Teams updateTeam(Teams team) {
        String sql = "UPDATE teams SET team_name = ?";
        jdbcTemplate.update(sql, team.getTeamName());
        return getTeamByTeamId(team.getTeamId());
    }
}
