package game.dao;
import game.model.QuestionCard;
import org.springframework.stereotype.Component;

public interface QuestionCardDao {
    QuestionCard getQuestionCard(QuestionCard questionCard);
}
