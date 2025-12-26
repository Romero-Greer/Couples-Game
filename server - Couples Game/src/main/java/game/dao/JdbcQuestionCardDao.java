package game.dao;

import game.model.QuestionCard;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcQuestionCardDao implements QuestionCardDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcQuestionCardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public QuestionCard getQuestionCard(QuestionCard questionCard) {
        QuestionCard questionCard1 = new QuestionCard();
        String sql = "SELECT * FROM question_cards WHERE question_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, questionCard.getCardId());
        if (results.next()) {
            questionCard1.setCardId(results.getInt("card_id"));
            questionCard1.setQuestion(results.getString("question"));
        }
        return questionCard1;
    }
}
