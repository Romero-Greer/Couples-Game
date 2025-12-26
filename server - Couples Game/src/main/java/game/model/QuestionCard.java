package game.model;

public class QuestionCard {
    private int cardId;
    private String question;

    public QuestionCard(){}

    public QuestionCard(int cardId, String question) {
        this.cardId = cardId;
        this.question = question;
    }
    public int getCardId() {
        return cardId;
    }
    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
}
