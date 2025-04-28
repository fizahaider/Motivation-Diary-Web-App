public class commentInfo {
    private int id;
    private int quoteId;
    private String username;
    private String comment;

    public commentInfo() {
        this.id = 0;
        this.quoteId = 0;
        this.username = "";
        this.comment = "";
    }

    public int getId() {
        return id;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
