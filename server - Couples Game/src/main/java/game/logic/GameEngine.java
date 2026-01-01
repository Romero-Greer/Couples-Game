package game.logic;
import game.model.QuestionCard;
import game.model.Teams;
import game.dao.PlayersDao;
import game.dao.TeamsDao;
import game.dao.QuestionCardDao;

import java.util.*;

public class GameEngine {
    private Scanner input = new Scanner(System.in);
    private final PlayersDao playersDao;
    private final TeamsDao teamsDao;
    private final QuestionCardDao questionCardDao;
    private List<Teams> allTeams = new ArrayList<>();
    private Teams currentTeam;
    private GamePhase currentPhase;
    private Map<Integer, Integer> rollOffResults;

    public GameEngine(PlayersDao playersDao, TeamsDao teamsDao, QuestionCardDao questionCardDao) {
        this.playersDao = playersDao;
        this.teamsDao = teamsDao;
        this.questionCardDao = questionCardDao;
        this.currentPhase = GamePhase.STARTUP;
        this.rollOffResults = new HashMap<>();
    }

    public void startGame() {
        this.allTeams = teamsDao.getTeams();
        if (allTeams.isEmpty()) {
            System.out.println("There are no teams to start. Please add teams to play!");
        }
        this.currentPhase = GamePhase.ROLL_OFF;
        System.out.println("Roll the dice to see who starts!");
        // Should I call the initialRoll method here?
    }

    /*public void handleRollOff() {
        List<Teams> teams = teamsDao.getTeams();
        Teams nextTeamToRoll = teams.get(0);

        while (nextTeamToRoll != null) {
            System.out.println(nextTeamToRoll.getTeamName() + ", press enter to roll!");
            input.nextLine();

            int roll = (int)(Math.random() * 6) + 1;
            System.out.println("Your team rolled a " + roll + "!");
            nextTeamToRoll = initialRoll(nextTeamToRoll, roll);
        }
    }*/

    public Teams initialRoll(Teams team, int rollAmount) {
        if (currentPhase != GamePhase.ROLL_OFF) {
            throw new IllegalStateException("Cannot roll now");
        }
        rollOffResults.put(team.getTeamId(), rollAmount);

        if (rollOffResults.size() == allTeams.size()) {
            determineFirstPlayer();
            return null;
        } else {
            System.out.println("Next team roll");
            int nextTeamIndex = rollOffResults.size();
            return allTeams.get(nextTeamIndex);
        }
    }

    public void determineFirstPlayer() {
        this.currentPhase = GamePhase.DETERMINING_FIRST_PLAYER;

        allTeams.sort((teamA, teamB) -> {
            int scoreA = rollOffResults.get(teamA.getTeamId());
            int scoreB = rollOffResults.get(teamB.getTeamId());
            return Integer.compare(scoreB, scoreA);
        });
        System.out.println("The starting order is: ");
        for (int i = 0; i < allTeams.size(); i++) {
            System.out.println((i + 1) + " : " + allTeams.get(i).getTeamName());
        }
        this.currentPhase = GamePhase.TEAM_TURN; //Not sure if this is right***
        playRound();
    }

    public void playRound() {
        System.out.println("Starting round");

        for (Teams team : allTeams) {
            this.currentTeam = team;
            teamTurn(team);
        }
        /** After everyone goes add a check if game is over or starts another round **/
    }

    public void teamTurn(Teams team) {
        List<QuestionCard> allQuestions = questionCardDao.getQuestionCards();

        Random random = new Random();
        int randomIndex = random.nextInt(allQuestions.size());
        QuestionCard question = allQuestions.get(randomIndex);
        String sameQuestion = question.getQuestion();

        System.out.println("It is " + team.getTeamName() + "'s turn!");
        System.out.println("Player 2 turn around. Player 1 give your answer.");
        System.out.println("Question: " + question.getQuestion());
        String player1Answer = input.nextLine();
        System.out.println("Player 2, submit your answer about Player 1");
        System.out.println("Question: " + sameQuestion);
        String player2Answer = input.nextLine();

        if (player1Answer.equals(player2Answer)) {
            System.out.println("ME, A WINNNNERRR!");
        } else {
            System.out.println("A TOWN FULL OF LOOOOOSSSSERRRSSS");
        }
    }
}

/** Extra Code Just I removed but saved just in case **/

/*int highestRoll = 0;
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
            rollOffResults.getKey
        }
*/
