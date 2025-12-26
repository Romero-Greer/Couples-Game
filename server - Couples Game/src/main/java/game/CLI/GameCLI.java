package game.CLI;
import game.dao.PlayersDao;
import game.dao.QuestionCardDao;
import game.dao.TeamsDao;
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
        /*System.out.println("Create your team!");
        System.out.println("What is your team name? " );*/

        List<Players> testPlayers = playersDao.getPlayers();
        List<Teams> testTeams = teamsDao.getTeams();
        //Try adding a get team by teamId so that the toString method will work per team
        //System.out.println(testTeams);
        System.out.println("---TEST DATABASE CONNECTION---");
        System.out.println("current number of players: " + testPlayers.size());
        System.out.println("current number of teams: " + testTeams.size());

        teamCreation();
    }

    public void teamCreation() {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter team name: ");
        String teamName = input.nextLine();
        Teams newTeam = new Teams(teamName);
        Teams team1 = teamsDao.addTeam(newTeam);

        System.out.println("Enter player one name: ");
        String player1Name = input.nextLine();
        Players newPlayer1 = new Players(player1Name, team1.getTeamId());
        Players player1 = playersDao.addPlayer(newPlayer1);

        System.out.println("Enter player two name: ");
        String player2Name = input.nextLine();
        Players newPlayer2 = new Players(player2Name, team1.getTeamId());
        Players player2 = playersDao.addPlayer(newPlayer2);
    }
}
