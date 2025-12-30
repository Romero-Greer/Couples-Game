package game.model;

public class Players {
    private int playerId;
    private String name;
    private int teamId;

    public Players() {}

    public Players(String name, int teamId) {
        this.name = name;
        this.teamId = teamId;
    }

    public Players(String name){
        this.name = name;
    }

    public Players(int playerId, String name, int teamId) {
        this.playerId = playerId;
        this.name = name;
        this.teamId = teamId;
    }
    public int getPlayerId() {
        return playerId;
    }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getTeamId() {
        return teamId;
    }
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    /*@Override
    public String toString() {
        return "Players : " +
    }*/
}
