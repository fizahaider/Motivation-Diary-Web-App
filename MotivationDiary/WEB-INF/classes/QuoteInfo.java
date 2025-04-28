public class QuoteInfo 
{
    private int id;         
    private String quote;   

    public QuoteInfo() {
        id = 0;
        quote = "";
    }

    public int getId() {
        return id;
    }

    public String getQuote() {
        return quote;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
}