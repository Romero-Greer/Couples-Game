package game.CLI;
import game.dao.PlayersDao;
import game.dao.QuestionCardDao;
import game.dao.TeamsDao;
import game.logic.GameEngine;
import game.model.Players;
import game.model.Teams;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Scanner;

import java.util.List;

@Component
public class GameCLI implements CommandLineRunner{
    private Scanner input = new Scanner(System.in);
    private PlayersDao playersDao;
    private TeamsDao teamsDao;
    private QuestionCardDao questionCardDao;

    public GameCLI(PlayersDao playersDao, TeamsDao teamsDao, QuestionCardDao questionCardDao) {
        this.playersDao = playersDao;
        this.teamsDao = teamsDao;
        this.questionCardDao = questionCardDao;
    }

    /** Couple things to work on next:
     * Adding delete team option
     * Adding in the return to home page within each of the choices**/
    @Override
    public void run(String... args) throws Exception {

        List<Players> testPlayers = playersDao.getPlayers();
        List<Teams> testTeams = teamsDao.getTeams();
        System.out.println("---TEST DATABASE CONNECTION---");
        System.out.println("current number of players: " + testPlayers.size());
        System.out.println("current number of teams: " + testTeams.size());

        startMenu();
    }

    private void teamCreation() {
        System.out.println("Enter team name: ");
        String teamName = input.nextLine();
        Teams newTeam = new Teams(teamName);
        Teams team1 = teamsDao.addTeam(newTeam);

        System.out.println("Enter player 1 name: ");
        String player1Name = input.nextLine();
        Players newPlayer1 = new Players(player1Name, team1.getTeamId());
        playersDao.addPlayer(newPlayer1);

        System.out.println("Enter player 2 name: ");
        String player2Name = input.nextLine();
        Players newPlayer2 = new Players(player2Name, team1.getTeamId());
        playersDao.addPlayer(newPlayer2);

        System.out.println("Welcome Team " + teamName + "!");
        startMenu();
    }

    private void startMenu(){
        GameEngine  gameEngine = new GameEngine(playersDao, teamsDao, questionCardDao);
        boolean done = false;
        System.out.println("Welcome to the newest Couples Game! Time to find out which couple knows each other the best...");
        while(!done) {
            printMenuSelection();
            int choice = promptForSelection("What would you like to do?");
            if (choice == 0) {
                gameEngine.startGame();
            } else if (choice == 1) {
                teamCreation();
            } else if (choice == 2) {
                printTeamsSelectionMenu();
                int teamChoice = promptForSelection("Which team would you like to update?");
                if (teamChoice == 0) {
                    printTeamUpdateSelections();
                    int updateChoice = promptForSelection("What would you like to update?");
                    if (updateChoice == 1) {
                        updateTeamName(teamChoice);
                    } else if (updateChoice == 2) {
                        updatePlayersNames(teamChoice);
                    } else {
                        System.out.println("Invalid choice. Try again.");
                    }
                } else if (teamChoice == 1) {
                    printTeamUpdateSelections();
                    int updateChoice = promptForSelection("What would you like to update?");
                    if (updateChoice == 1) {
                        updateTeamName(teamChoice);
                    } else if (updateChoice == 2) {
                        updatePlayersNames(teamChoice);
                    } else {
                        System.out.println("Invalid choice. Try again.");
                    }
                } else if (teamChoice == 2) {
                    printTeamUpdateSelections();
                    int updateChoice = promptForSelection("What would you like to update?");
                    if (updateChoice == 1) {
                        updateTeamName(teamChoice);
                    } else if (updateChoice == 2) {
                        updatePlayersNames(teamChoice);
                    } else {
                        System.out.println("Invalid choice. Try again.");
                    }
                } else if (teamChoice == 3) {
                    printTeamUpdateSelections();
                    int updateChoice = promptForSelection("What would you like to update?");
                    if (updateChoice == 1) {
                        updateTeamName(teamChoice);
                    } else if (updateChoice == 2) {
                        updatePlayersNames(teamChoice);
                    } else {
                        System.out.println("Invalid choice. Try again.");
                    }
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            } else if (choice == 4) {
                getTeams();
            } else if (choice == 5) {
                done = true;
                System.out.println("Thanks for playing. See you soon!");
            } else {
                System.out.println("Invalid choice");
            }
        }
    }

    private void printMenuSelection(){
        System.out.println("HOME");
        System.out.println("(0): Start Game");
        System.out.println("(1): Add Team");
        System.out.println("(2): Update Team");
        System.out.println("(4): View Teams");
        System.out.println("(5): Quit Game");
    }

    private int promptForSelection(String prompt) {
        System.out.println(prompt);
        int menuSelection = 0;
        try {
            menuSelection = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    private void printTeamUpdateSelections() {
        System.out.println("(1): Team Name");
        System.out.println("(2): Players");
        //System.out.println("(3): Player 2");
    }

    private void updateTeamName(int element) {
        System.out.println("Enter team name: ");
        String teamName = input.nextLine();
        List<Teams> teams = teamsDao.getTeams();
        Teams team = teams.get(element);
        int teamId = team.getTeamId();
        Teams updatedTeamName = new Teams(teamId, teamName);
        teamsDao.updateTeam(updatedTeamName);
    }

    private void printTeamsSelectionMenu(){
        List<Teams> teamNames = teamsDao.getTeams();
        int teamId = -1;
        for (Teams team : teamNames) {
            String teamName = team.getTeamName();
            teamId = teamId + 1;
            System.out.println("(" + teamId + "): " + teamName);
        }
    }

    private void updatePlayersNames(int element){
        List<Teams> teams = teamsDao.getTeams();
        Teams team = teams.get(element);
        int teamId = team.getTeamId();

        System.out.println("Enter player 1 name: ");
        String player1Name = input.nextLine();
        Players updatedPlayer1 = new Players(player1Name, teamId);
        playersDao.updatePlayer(updatedPlayer1);

        System.out.println("Enter player 2 name: ");
        String player2Name = input.nextLine();
        Players updatedPlayer2 = new Players(player2Name, teamId);
        playersDao.updatePlayer(updatedPlayer2);
    }

    private void getTeams() {
        List<Teams> teams = teamsDao.getTeams();
        int teamNumber = 1;

        for (Teams team : teams) {
            System.out.println("Team " + teamNumber + ": " + team.getTeamName());
            teamNumber++;
            List<Players> players = playersDao.getPlayersByTeamId(team.getTeamId());
            int playerNumber = 1;
            for (Players player : players) {
                System.out.println("Player " + playerNumber + ": " + player.getName());
                playerNumber++;
            }
        }
    }
}
