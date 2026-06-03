package game.logic;
import game.model.QuestionCard;
import game.model.Scores;
import game.model.Teams;
import game.dao.PlayersDao;
import game.dao.ScoresDao;
import game.dao.TeamsDao;
import game.dao.QuestionCardDao;

import java.util.*;

public class GameEngine {
    private Scanner input = new Scanner(System.in);
    private final PlayersDao playersDao;
    private final TeamsDao teamsDao;
    private final QuestionCardDao questionCardDao;
    private final ScoresDao scoresDao;
    private List<Teams> allTeams = new ArrayList<>();
    private List<Teams> tiedTeams = new ArrayList<>();
    private Teams currentTeam;
    private GamePhase currentPhase;
    private Map<Integer, Integer> rollOffResults;
    private Map<Integer, Integer> teamTurnCount = new HashMap<>();

    public GameEngine(PlayersDao playersDao, TeamsDao teamsDao, QuestionCardDao questionCardDao, ScoresDao scoresDao) {
        this.playersDao = playersDao;
        this.teamsDao = teamsDao;
        this.questionCardDao = questionCardDao;
        this.scoresDao = scoresDao;
        this.currentPhase = GamePhase.STARTUP;
        this.rollOffResults = new HashMap<>();
    }

    public List<Teams> getAllTeams() {
        return allTeams;
    }

    public List<Teams> getTiedTeams() {
        return tiedTeams;
    }

    private void detectTies() {
        tiedTeams.clear();
        for (int i = 0; i < allTeams.size() - 1; i++) {
            int scoreA = rollOffResults.get(allTeams.get(i).getTeamId());
            int scoreB = rollOffResults.get(allTeams.get(i + 1).getTeamId());
            if (scoreA == scoreB) {
                if (!tiedTeams.contains(allTeams.get(i))) {
                    tiedTeams.add(allTeams.get(i));
                }
                tiedTeams.add(allTeams.get(i + 1));
            }
        }
    }

    public Teams tieBreakRoll(Teams team, int rollAmount) {
        rollOffResults.put(team.getTeamId(), rollAmount);
        int nextIndex = tiedTeams.indexOf(team) + 1;
        if (nextIndex >= tiedTeams.size()) {
            allTeams.sort((teamA, teamB) -> {
                int scoreA = rollOffResults.get(teamA.getTeamId());
                int scoreB = rollOffResults.get(teamB.getTeamId());
                return Integer.compare(scoreB, scoreA);
            });
            detectTies();
            return null;
        }
        return tiedTeams.get(nextIndex);
    }

    public void startGame() {
        this.allTeams = teamsDao.getTeams();
        if (allTeams.isEmpty()) {
            System.out.println("There are no teams to start. Please add teams to play!");
            return;
        }
        scoresDao.initializeScores(allTeams);
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
        detectTies();
        this.currentPhase = GamePhase.TEAM_TURN;
    }

    public Teams playRound() {
        System.out.println("Starting round");

        for (Teams team : allTeams) {
            this.currentTeam = team;
            boolean gameOver = teamTurn(team);
            if (gameOver) {
                return team;
            }
        }
        return null;
    }

    public void resetForNewGame() {
        rollOffResults.clear();
        tiedTeams.clear();
        teamTurnCount.clear();
        scoresDao.resetAllScores();
        this.currentPhase = GamePhase.ROLL_OFF;
        System.out.println("Roll the dice to see who starts!");
    }

    public void wipeAllData() {
        teamsDao.deleteAllTeams();
    }

    public void displayScoreboard() {
        System.out.println("--- SCOREBOARD ---");
        List<Scores> scores = scoresDao.getScores();
        for (int i = 0; i < scores.size(); i++) {
            Scores s = scores.get(i);
            System.out.println((i + 1) + ". " + s.getTeamName() + " — " + s.getScore() + " pts");
        }
        System.out.println("------------------");
    }

    public boolean teamTurn(Teams team) {
        List<QuestionCard> allQuestions = questionCardDao.getQuestionCards();
        if (allQuestions.isEmpty()) {
            System.out.println("No question cards found. Please add questions to the database.");
            return false;
        }

        List<game.model.Players> players = playersDao.getPlayersByTeamId(team.getTeamId());
        if (players.size() < 2) {
            System.out.println("Team " + team.getTeamName() + " does not have 2 players. Skipping turn.");
            return false;
        }

        int turnCount = teamTurnCount.getOrDefault(team.getTeamId(), 0);
        String answerer = (turnCount % 2 == 0) ? players.get(0).getName() : players.get(1).getName();
        String guesser  = (turnCount % 2 == 0) ? players.get(1).getName() : players.get(0).getName();

        Random random = new Random();
        int randomIndex = random.nextInt(allQuestions.size());
        QuestionCard question = allQuestions.get(randomIndex);

        System.out.println("--- " + team.getTeamName().toUpperCase() + "'S TURN ---");
        System.out.println(guesser + ", please look away!");
        System.out.println("Press enter when " + guesser + " has looked away...");
        input.nextLine();

        System.out.println(answerer + ", answer this question about yourself:");
        System.out.println("Question: " + question.getQuestion());
        String answererResponse = input.nextLine();

        System.out.println("---");
        System.out.println(answerer + ", hide your answer. " + guesser + ", you can look now!");
        System.out.println("Press enter when " + guesser + " is ready...");
        input.nextLine();

        System.out.println(guesser + ", what do you think " + answerer + " answered?");
        System.out.println("Question: " + question.getQuestion());
        String guesserResponse = input.nextLine();

        System.out.println("---");
        System.out.println(answerer + " answered: " + answererResponse);
        System.out.println(guesser + "  guessed:  " + guesserResponse);

        teamTurnCount.put(team.getTeamId(), turnCount + 1);

        if (StringSimilarity.isSimilarEnough(answererResponse, guesserResponse)) {
            System.out.println("It's a match!");
            Scores updated = scoresDao.addPointToTeam(team.getTeamId());
            displayScoreboard();
            if (updated != null && updated.getScore() >= 10) {
                return true;
            }
        } else {
            System.out.println("Not a match.");
            displayScoreboard();
        }

        return false;
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
