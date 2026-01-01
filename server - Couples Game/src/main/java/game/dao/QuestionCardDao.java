package game.dao;
import game.model.QuestionCard;
import org.springframework.stereotype.Component;

import java.util.List;

public interface QuestionCardDao {
   List<QuestionCard> getQuestionCards();
}
