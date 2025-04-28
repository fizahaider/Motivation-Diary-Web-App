import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
public class addcomment extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);  
        if (session == null || "admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        String quoteId = request.getParameter("quoteId");
        String username = request.getParameter("username");
        String comment = request.getParameter("comment");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Confirm Comment</title>");
        out.println("<link rel='stylesheet' type='text/css' href='c.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>Confirm Your Comment</h1>");
        out.println("<div class='comment-preview'>");
        out.println("<p>" + comment + "</p>");
        out.println("</div>");
        out.println("<form method='POST' action='addcomment'>");
        out.println("<input type='hidden' name='quoteId' value='" + quoteId + "'>");
        out.println("<input type='hidden' name='username' value='" + username + "'>");
        out.println("<input type='hidden' name='comment' value='" + comment + "'>");
        out.println("<div class='form-group'>");
        out.println("<button type='submit' class='button'>Confirm Comment</button>");
        out.println("<a href='viewquotes' class='button'>Cancel</a>");
        out.println("</div>");
        out.println("</form>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quoteId = request.getParameter("quoteId");
        String username = request.getParameter("username");
        String comment = request.getParameter("comment");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Comment Submission</title>");
        out.println("<link rel='stylesheet' type='text/css' href='c.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        
        try {   
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/motivationdiary", "root", "root");
            String query = "INSERT INTO comments (quoteId, username, comment) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, quoteId);
            pstmt.setString(2, username);
            pstmt.setString(3, comment);
            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                out.println("<h1>Comment Added Successfully!</h1>");
                out.println("<div class='success-message'>");
                out.println("<svg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'><path d='M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z'/></svg>");
                out.println("<p>Your comment has been posted successfully.</p>");
                out.println("</div>");
            } else {
                out.println("<h1>Failed to Add Comment</h1>");
                out.println("<div class='error-message'>");
                out.println("<svg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'><path d='M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z'/></svg>");
                out.println("<p>There was an error processing your request.</p>");
                out.println("</div>");
            }
            
            out.println("<div class='form-group'>");
            out.println("<a href='viewquotes' class='button'>Back to Quotes</a>");
            out.println("</div>");

            pstmt.close();
            con.close();
        } catch (Exception e) {
            out.println("<h1>Failed to Add Comment</h1>");
            out.println("<div class='error-message'>");
            out.println("<svg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'><path d='M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z'/></svg>");
            out.println("<p>An exception occurred: " + e.getMessage() + "</p>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<a href='viewquotes' class='button'>Back to Quotes</a>");
            out.println("</div>");
        }
        
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}