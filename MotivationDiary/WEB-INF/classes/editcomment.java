import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
public class editcomment extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);  
        if (session == null || "admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        String commentId = request.getParameter("commentId");
        String comment = null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Edit Comment</title>");
        out.println("<link rel='stylesheet' type='text/css' href='c.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/motivationdiary", "root", "root");
            String query = "SELECT comment FROM comments WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, commentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                comment = rs.getString("comment");
                
                out.println("<h1>Edit Your Comment</h1>");
                out.println("<form method='POST' action='editcomment'>");
                out.println("<input type='hidden' name='commentId' value='" + commentId + "'>");
                out.println("<div class='form-group'>");
                out.println("<label for='comment'>Comment:</label>");
                out.println("<textarea id='comment' name='comment' required>" + comment + "</textarea>");
                out.println("</div>");
                out.println("<div class='form-group'>");
                out.println("<button type='submit' class='button'>Save Changes</button>");
                out.println("<a href='viewquotes' class='button'>Cancel</a>");
                out.println("</div>");
                out.println("</form>");
            } else {
                out.println("<h1>Error</h1>");
                out.println("<div class='error-message'>");
                out.println("<p>Comment not found.</p>");
                out.println("</div>");
                out.println("<a href='viewquotes' class='button'>Back to Quotes</a>");
            }
            
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            out.println("<h1>Error</h1>");
            out.println("<div class='error-message'>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("</div>");
            out.println("<a href='viewquotes' class='button'>Back to Quotes</a>");
        }

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commentId = request.getParameter("commentId");
        String comment = request.getParameter("comment");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Comment Updated</title>");
        out.println("<link rel='stylesheet' type='text/css' href='c.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/motivationdiary", "root", "root");
            String query = "UPDATE comments SET comment = ? WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, comment);
            pstmt.setString(2, commentId);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                out.println("<h1>Comment Updated</h1>");
                out.println("<div class='success-message'>");
                out.println("<svg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'><path d='M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z'/></svg>");
                out.println("<p>Your comment was updated successfully.</p>");
                out.println("</div>");
            } else {
                out.println("<h1>Update Failed</h1>");
                out.println("<div class='error-message'>");
                out.println("<svg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'><path d='M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z'/></svg>");
                out.println("<p>Comment could not be updated.</p>");
                out.println("</div>");
            }

            out.println("<div class='form-group'>");
            out.println("<a href='viewquotes' class='button'>Back to Quotes</a>");
            out.println("</div>");

            pstmt.close();
            con.close();
        } catch (Exception e) {
            out.println("<h1>Error</h1>");
            out.println("<div class='error-message'>");
            out.println("<svg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'><path d='M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z'/></svg>");
            out.println("<p>" + e.getMessage() + "</p>");
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