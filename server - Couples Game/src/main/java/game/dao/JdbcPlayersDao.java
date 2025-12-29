package game.dao;

import game.model.Players;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcPlayersDao implements PlayersDao {
    private final JdbcTemplate jdbcTemplate;
    public JdbcPlayersDao(JdbcTemplate jt) {
        this.jdbcTemplate = jt;
    }
    private Players mapPlayersToRow(SqlRowSet rs) {
        Players player = new Players();
        player.setPlayerId(rs.getInt("player_id"));
        player.setName(rs.getString("name"));
        player.setTeamId(rs.getInt("team_id"));
        return player;
    }

    @Override
    public List<Players> getPlayers() {
        List<Players> players = new ArrayList<>();
        String sql = "SELECT * FROM players";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            players.add(mapPlayersToRow(results));
        }
        return players;
    }

    @Override
    public Players getPlayerByPlayerId(int playerId) {
        Players player = null;
        String sql = "SELECT * FROM players WHERE player_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, playerId);
        if (results.next()) {
            player = mapPlayersToRow(results);
        }
        return player;
    }

    @Override
    public List<Players> getPlayersByTeamId(int teamId) {
        List<Players> players = null;
        String sql = "SELECT * FROM players WHERE team_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, teamId);
            while (results.next()) {
                players.add(mapPlayersToRow(results));
            }
        return players;
    }

    @Override
    public Players addPlayer(Players player) {
        String sql = "INSERT INTO players (name, team_id) VALUES (?,?) RETURNING player_id";
        int newPlayerId = jdbcTemplate.queryForObject(sql, int.class, player.getName(), player.getTeamId());
        return getPlayerByPlayerId(newPlayerId);
    }

    @Override
    public List<Players> deletePlayer(int playerId) {
        String sql = "DELETE FROM players WHERE player_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, playerId);
        if (rowsAffected > 0) {
            return getPlayers();
        }
        System.out.println("Unable to remove player.");
        return getPlayers();
    }

    @Override
    public Players updatePlayer(Players player) {
        String sql = "UPDATE players SET name = ?, team_id = ? WHERE player_id = ?";

        jdbcTemplate.update(sql, player.getName(), player.getTeamId());

        return getPlayerByPlayerId(player.getPlayerId());
    }

    @Override
    public Players getPlayerFromTeam(Players player) {
        String sql = "SELECT * FROM players WHERE team_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, player.getTeamId());

        if (results.next()) {
            player = mapPlayersToRow(results);
        }
        return player;
    }
}
