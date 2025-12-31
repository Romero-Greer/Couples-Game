package game;
import game.logic.GameEngine;
import game.dao.TeamsDao;
import game.dao.PlayersDao;
import game.dao.QuestionCardDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
