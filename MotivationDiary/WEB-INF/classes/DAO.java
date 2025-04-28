import java.sql.*;
import java.util.ArrayList;

class DAO {
    private Connection con;

    public DAO() throws ClassNotFoundException, SQLException {
        establishConnection();
    }

    private void establishConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/motivationdiary", "root", "root");
    }

    
    public ArrayList<personInfo> viewPersons(String name) throws SQLException {
        ArrayList<personInfo> personList = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE name = ?";
        PreparedStatement pStmt = con.prepareStatement(sql);
        pStmt.setString(1, name);

        ResultSet rs = pStmt.executeQuery();
        while (rs.next()) {
            personInfo person = new personInfo();
            person.setName(rs.getString("name"));
            person.setPassword(rs.getString("password"));
            person.setRole(rs.getString("role"));

            personList.add(person);
        }
        return personList;
    }

    //add user y 
    public int addPerson(String name , String password , String role) throws SQLException {
        String sql = "INSERT INTO users (name, password, role) VALUES (?, ?, ?)";
        PreparedStatement pStmt = con.prepareStatement(sql);
        pStmt.setString(1, name);
        pStmt.setString(2, password);
        pStmt.setString(3, role);
        int ans = pStmt.executeUpdate();
        return ans;
    }

    public int addQuote(String quote) throws SQLException {
        String sql = "INSERT INTO quotes (quote) VALUES (?)";
        PreparedStatement pStmt = con.prepareStatement(sql);
        pStmt.setString(1, quote);
        return pStmt.executeUpdate();
    }

    public int addComment(int quoteId, String username, String comment) throws SQLException {
        String sql = "INSERT INTO comments (quoteId, username, comment) VALUES (?, ?, ?)";
        PreparedStatement pStmt = con.prepareStatement(sql);
        pStmt.setInt(1, quoteId);
        pStmt.setString(2, username);
        pStmt.setString(3, comment);
        return pStmt.executeUpdate();
    }
    
    public commentInfo readCommentsByQuote(String quoteId) throws SQLException {
        String sql = "SELECT * FROM COMMENTS WHERE quoteId = ?";
        commentInfo com = new commentInfo();  // Create a new commentInfo object
        PreparedStatement pStmt = con.prepareStatement(sql);
        pStmt.setString(1, quoteId);
        
        ResultSet rs = pStmt.executeQuery();
    
        if (rs.next()) {
            com.setId(rs.getInt("id"));
            com.setQuoteId(rs.getInt("quoteId"));
            com.setUsername(rs.getString("username"));
            com.setComment(rs.getString("comment"));
        }
        rs.close();  
        pStmt.close(); 
        return com; 
    }
    public int deleteComment(int commentId, String username, String role) throws SQLException {
        String query = "DELETE FROM comments WHERE id = ? AND (username = ? OR ? = 'admin')";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, commentId);
        pstmt.setString(2, username);
        pstmt.setString(3, role);
        int result = pstmt.executeUpdate();  
        return result;
    }
    public int updateComment(int commentId, String comment) throws SQLException {
        String query = "UPDATE comments SET comment = ? WHERE id = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, comment);  
        pstmt.setInt(2, commentId);  
        int rowsUpdated = pstmt.executeUpdate();
        return rowsUpdated;  
    }

    public QuoteInfo readQuoteById(int quoteId) throws SQLException {
        String sql = "SELECT * FROM quotes WHERE id = ?";
        QuoteInfo quote = new QuoteInfo();  
        PreparedStatement pStmt = con.prepareStatement(sql);
        pStmt.setInt(1, quoteId);  
        
        ResultSet rs = pStmt.executeQuery();
        
        if (rs.next()) {
            quote.setId(rs.getInt("id"));
            quote.setQuote(rs.getString("quote"));
        }
        rs.close();  
        pStmt.close(); 
        return quote; 
    }

    public int deleteQuote(int quoteId, String quote) throws SQLException {
        String query = "DELETE FROM quotes WHERE id = ? AND (quote = ? )";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, quoteId);  // Set the quoteId in the query
        pstmt.setString(2, quote);  // Set the username
        
        int result = pstmt.executeUpdate();  // Execute the delete query
        return result;  
    }
    
    public int updateQuote(int quoteId, String quote) throws SQLException {
        String query = "UPDATE quotes SET quote = ? WHERE id = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, quote);  
        pstmt.setInt(2, quoteId); 
        int rowsUpdated = pstmt.executeUpdate();
        return rowsUpdated;
    }
}
    