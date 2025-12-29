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

        menuSelection();
    }
    /** I need to change this menuSelection method to a print menu selection.
     * Also need to take the scanner code out of that method and put into a separate method
     * so that when i quit game it doesn't move onto the next method. Probably need
     * to put it into a while loop or something. Check proj 1 in module 1 for help.
     *
     * Next I can add update and delete team options.**/
    private void teamCreation() {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter team name: ");
        String teamName = input.nextLine();
        Teams newTeam = new Teams(teamName);
        Teams team1 = teamsDao.addTeam(newTeam);

        System.out.println("Enter player one name: ");
        String player1Name = input.nextLine();
        Players newPlayer1 = new Players(player1Name, team1.getTeamId());
        playersDao.addPlayer(newPlayer1);

        System.out.println("Enter player two name: ");
        String player2Name = input.nextLine();
        Players newPlayer2 = new Players(player2Name, team1.getTeamId());
        playersDao.addPlayer(newPlayer2);

        System.out.println("Welcome Team " + teamName + "!");
        menuSelection();
    }

    private void menuSelection(){
        Scanner input = new Scanner(System.in);
        GameEngine  gameEngine = new GameEngine(playersDao, teamsDao, questionCardDao);
        System.out.println("HOME");
        System.out.println("(1): Start Game");
        System.out.println("(2): Add Team");

        System.out.println("(): Quit Game");
        System.out.println("What would you like to do?");
        int choice = input.nextInt();
        boolean done = false;
        while(!done) {
            if (choice == 1) {
                gameEngine.startGame();
            } else if (choice == 2) {
                teamCreation();
            } else if (choice == 3) {
                done = true;
                System.out.println("Thanks for playing. See you soon!");
            } else {
                System.out.println("Invalid choice");
            }
        }
    }
}
