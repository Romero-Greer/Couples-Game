package game.model;

public class QuestionCard {
    private int cardId;
    private String question;
    private String cardType;

    public QuestionCard(){}

    public QuestionCard(int cardId, String question, String cardType) {
        this.cardId = cardId;
        this.question = question;
        this.cardType = cardType;
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
    public String getCardType() {
        return cardType;
    }
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
