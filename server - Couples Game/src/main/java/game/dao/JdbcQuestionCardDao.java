package game.dao;

import game.model.QuestionCard;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcQuestionCardDao implements QuestionCardDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcQuestionCardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public QuestionCard mapQuestionCardToRow(SqlRowSet rs) {
        QuestionCard questionCard = new QuestionCard();
        questionCard.setCardId(rs.getInt("card_id"));
        questionCard.setQuestion(rs.getString("question"));
        return questionCard;
    }

    @Override
    public List<QuestionCard> getQuestionCards() {
        List<QuestionCard> questionCards = new ArrayList<>();
        String sql = "SELECT * FROM question_cards";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            questionCards.add(mapQuestionCardToRow(results));
        }
        return questionCards;
    }
}
