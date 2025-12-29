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
    @Override
    public void run(String... args) throws Exception {

        List<Players> testPlayers = playersDao.getPlayers();
        List<Teams> testTeams = teamsDao.getTeams();
        System.out.println("---TEST DATABASE CONNECTION---");
        System.out.println("current number of players: " + testPlayers.size());
        System.out.println("current number of teams: " + testTeams.size());

        startMenu();
    }
    /** I need to change this menuSelection method to a print menu selection.
     * Also need to take the scanner code out of that method and put into a separate method
     * so that when i quit game it doesn't move onto the next method. Probably need
     * to put it into a while loop or something. Check proj 1 in module 1 for help.
     *
     * Next I can add update and delete team options.**/
    private void teamCreation() {
        //Scanner input = new Scanner(System.in);

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
        //Scanner input = new Scanner(System.in);
        GameEngine  gameEngine = new GameEngine(playersDao, teamsDao, questionCardDao);
        /*System.out.println("HOME");
        System.out.println("(1): Start Game");
        System.out.println("(2): Add Team");

        System.out.println("(): Quit Game");*/
        //System.out.println("What would you like to do?");

        //int choice = input.nextInt();
        boolean done = false;
        System.out.println("Welcome to the newest Couples Game! Time to find out which couple knows each other the best...");
        while(!done) {
            printMenuSelection();
            int choice = promptForSelection("What would you like to do?");
            if (choice == 1) {
                gameEngine.startGame();
            } else if (choice == 2) {
                teamCreation();
            } else if (choice == 3) {
                printTeamUpdateSelections();
                int teamChoice = promptForSelection("What would you like to update?");
                if (teamChoice == 1) {
                    //teamsDao.updateTeam()
                    /** I need to find a way to access the team and update the team name.
                     * Basically, I need to create a update team method in this class that
                     * doesn't take in any parameters. Have that method call the update team
                     * method in the jdbc class.**/
                }
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
        System.out.println("(1): Start Game");
        System.out.println("(2): Add Team");

        System.out.println("(3): Quit Game");
        //System.out.println("What would you like to do?");
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
        //System.out.println("What would you like to update?");
        System.out.println("(1): Team Name");
        System.out.println("(2): Player 1");
        System.out.println("(3): Player 2");
    }
}
