import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class viewquotes extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);

        // Check authentication
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String role = (String) session.getAttribute("role");
        String username = (String) session.getAttribute("username");

        try {
            // Start HTML output with proper CSS linking
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>View Quotes</title>");
            out.println("<link rel='stylesheet' href='c.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");

            // Header with user info
            out.println("<div class='header'>");
            out.println("<h1>Motivational Quotes</h1>");
            out.println("<div class='user-controls'>");
            out.println("<span class='welcome'>Welcome, " + username + "!</span>");
            out.println("<a href='logout' class='button'>Log Out</a>");
            out.println("</div>");
            out.println("</div>");

            // Admin controls
            if ("admin".equals(role)) {
                out.println("<div class='admin-controls'>");
                out.println("<a href='addquote.html' class='button'>Add New Quote</a>");
                out.println("</div>");
            }

            // Database connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/motivationdiary", 
                "root", 
                "root"
            );

            // Get all quotes
            String query = "SELECT * FROM quotes ORDER BY id DESC";
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            out.println("<div class='quote-list'>");

            while (rs.next()) {
                int quoteId = rs.getInt("id");
                String quote = rs.getString("quote");

                // Quote container
                out.println("<div class='quote-item'>");
                out.println("<div class='quote-content'>");
                out.println("<p class='quote-text'>" + quote + "</p>");
                out.println("</div>");

                // Comments section
                out.println("<div class='comment-section'>");
                out.println("<h3>Comments:</h3>");

                // Get comments for this quote
                String commentQuery = "SELECT * FROM comments WHERE quoteId = ? ORDER BY id DESC";
                PreparedStatement commentStmt = con.prepareStatement(commentQuery);
                commentStmt.setInt(1, quoteId);
                ResultSet commentRs = commentStmt.executeQuery();

                if (!commentRs.isBeforeFirst()) {
                    out.println("<p class='no-comments'>No comments yet.</p>");
                }

                while (commentRs.next()) {
                    String comment = commentRs.getString("comment");
                    String commentUser = commentRs.getString("username");
                    int commentId = commentRs.getInt("id");

                    out.println("<div class='comment-item'>");
                    out.println("<p class='comment-author'>" + commentUser + ":</p>");
                    out.println("<p class='comment-text'>" + comment + "</p>");

                    // Comment controls
                    if ("admin".equals(role) || username.equals(commentUser)) {
                        out.println("<div class='comment-actions'>");
                        out.println("<form action='editcomment' method='GET' class='inline-form'>");
                        out.println("<input type='hidden' name='commentId' value='" + commentId + "'>");
                        out.println("<button type='submit' class='button small'>Edit</button>");
                        out.println("</form>");
                        
                        out.println("<form action='deletecomment' method='POST' class='inline-form'>");
                        out.println("<input type='hidden' name='commentId' value='" + commentId + "'>");
                        out.println("<button type='submit' class='button small danger'>Delete</button>");
                        out.println("</form>");
                        out.println("</div>");
                    }
                    out.println("</div>"); // Close comment-item
                }
                commentStmt.close();

                // Add comment form
                out.println("<form action='addcomment' method='POST' class='add-comment'>");
                out.println("<input type='hidden' name='quoteId' value='" + quoteId + "'>");
                out.println("<input type='hidden' name='username' value='" + username + "'>");
                out.println("<textarea name='comment' placeholder='Add a comment...' required></textarea>");
                out.println("<button type='submit' class='button'>Post Comment</button>");
                out.println("</form>");

                // Quote controls for admin
                if ("admin".equals(role)) {
                    out.println("<div class='quote-actions'>");
                    out.println("<form action='editquote' method='GET' class='inline-form'>");
                    out.println("<input type='hidden' name='id' value='" + quoteId + "'>");
                    out.println("<button type='submit' class='button small'>Edit Quote</button>");
                    out.println("</form>");
                    
                    out.println("<form action='deletequote' method='POST' class='inline-form'>");
                    out.println("<input type='hidden' name='id' value='" + quoteId + "'>");
                    out.println("<button type='submit' class='button small danger'>Delete Quote</button>");
                    out.println("</form>");
                    out.println("</div>");
                }

                out.println("</div>"); // Close comment-section
                out.println("</div>"); // Close quote-item
            }

            out.println("</div>"); // Close quote-list
            out.println("</div>"); // Close container
            out.println("</body>");
            out.println("</html>");

            // Clean up
            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            out.println("<div class='error-message'>");
            out.println("<h2>Error</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='viewquotes' class='button'>Try Again</a>");
            out.println("</div>");
        }
    }
}