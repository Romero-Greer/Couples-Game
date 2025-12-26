package game.logic;
import game.model.Teams;
import game.dao.PlayersDao;
import game.dao.TeamsDao;
import game.dao.QuestionCardDao;

import java.util.List;
import java.util.Map;

public class GameEngine {
    private final PlayersDao playersDao;
    private final TeamsDao teamsDao;
    private final QuestionCardDao questionCardDao;
    private List<Teams> allTeams;
    private Teams currentTeam;
    private GamePhase currentPhase;
    private Map<Teams, Integer> rollOffResults;

    public GameEngine(PlayersDao playersDao, TeamsDao teamsDao, QuestionCardDao questionCardDao) {
        this.playersDao = playersDao;
        this.teamsDao = teamsDao;
        this.questionCardDao = questionCardDao;
        this.currentPhase = GamePhase.STARTUP;
    }

    public void startGame() {
        teamsDao.getTeams();
        System.out.println("Roll the dice to see who starts!");

        this.currentPhase = GamePhase.ROLL_OFF;
        // Should I call the initialRoll method here?
    }

    public void initialRoll(Teams team, int rollAmount) {
        if (currentPhase != GamePhase.ROLL_OFF) {
            throw new IllegalStateException("Cannot roll now");
        }
        rollOffResults.put(team, rollAmount);

        if (rollOffResults.size() == allTeams.size()) {
            determineFirstPlayer();
        } else {
            System.out.println("Next team roll");
        }
        /* if (currentPhase != GamePhase.ROLL_OFF) {
            throw new IllegalStateException("Cannot roll now.");
        }
        rollOffResults.put(team, rollAmount);

        // Add logic here to check if ALL teams have rolled
        if (rollOffResults.size() == allTeams.size()) {
            determineFirstPlayer();
        } else {
            // Prompt the next team to roll
        } */
    }

    public void determineFirstPlayer() {
        this.currentPhase = GamePhase.DETERMINING_FIRST_PLAYER;
        int highestRoll = 0;
        int secondHighestRoll = 0;
        int thirdHighestRoll = 0;
        int fourthHighestRoll = 0;
        Teams winningTeam = null;
        Teams secondTeam = null;
        Teams thirdTeam = null;
        Teams fourthTeam = null;
        for (Map.Entry<Teams, Integer> entry : rollOffResults.entrySet()) {
            Teams currentTeam = entry.getKey();
            int currentRoll = entry.getValue();

            if (currentRoll > highestRoll) {
                fourthHighestRoll = thirdHighestRoll;
                thirdHighestRoll = secondHighestRoll;
                secondHighestRoll = highestRoll;
                highestRoll = currentRoll;
                fourthTeam = thirdTeam;
                thirdTeam = secondTeam;
                secondTeam = winningTeam;
                winningTeam = currentTeam;
            } else if (currentRoll > secondHighestRoll) {
                fourthHighestRoll = thirdHighestRoll;
                thirdHighestRoll = secondHighestRoll;
                secondHighestRoll = currentRoll;
                fourthTeam = thirdTeam;
                thirdTeam = secondTeam;
                secondTeam = currentTeam;
            } else if (currentRoll > thirdHighestRoll) {
                fourthHighestRoll = thirdHighestRoll;
                thirdHighestRoll = currentRoll;
                fourthTeam = thirdTeam;
                thirdTeam = currentTeam;
            } else {
                fourthHighestRoll = currentRoll;
                fourthTeam = currentTeam;
            }
        }
        this.currentPhase = GamePhase.TEAM_TURN; //Not sure if this is right***
    }

    public void teamTurn() {

    }


}
