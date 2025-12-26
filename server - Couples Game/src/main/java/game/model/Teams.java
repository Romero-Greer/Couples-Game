package game.model;

import java.util.List;

public class Teams {
    private int teamId;
    private String teamName;
    private List<Players> players;

    public Teams(){}
    public Teams(String teamName) {
        this.teamName = teamName;
    }

    public Teams(int teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }
    public int getTeamId() {
        return teamId;
    }
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<Players> getPlayers() {
        return players;
    }

    public void setPlayers(List<Players> players) {
        this.players = players;
    }

    @Override
    public String toString() {
          return "Teams: " + System.lineSeparator() +
                  "Team Name = " + teamName + System.lineSeparator() +
                  System.lineSeparator() +
                  "Players: " + System.lineSeparator();
                  /*"Player 1: " + players.getName + System.lineSeparator() +
                  "Player 2: " + players.getName;*/
    }
}
