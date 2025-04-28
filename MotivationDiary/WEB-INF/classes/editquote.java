import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class editquote extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        if (session == null || "user".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }
        Integer quoteId = (Integer) session.getAttribute("quoteId");

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Edit Quote</title>");
        out.println("<link rel='stylesheet' type='text/css' href='c.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");

        if (quoteId == null) {
            out.println("<h1>Error</h1>");
            out.println("<div class='error-message'>");
            out.println("<p>Quote ID is missing in the session.</p>");
            out.println("</div>");
            out.println("<a href='viewquotes' class='button'>Back to Quotes</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            return;
        }

        String quote = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/motivationdiary", "root", "root");

            String query = "SELECT quote FROM quotes WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, quoteId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                quote = rs.getString("quote");
                
                out.println("<h1>Edit Motivational Quote</h1>");
                out.println("<form action='editquote' method='POST'>");
                out.println("<div class='form-group'>");
                out.println("<label for='quoteText'>Quote:</label>");
                out.println("<input type='text' id='quoteText' name='quote' value='" + quote + "' required>");
                out.println("</div>");
                out.println("<input type='hidden' name='id' value='" + quoteId + "'>");
                out.println("<div class='form-group'>");
                out.println("<button type='submit' class='button'>Save Changes</button>");
                out.println("<a href='viewquotes' class='button'>Cancel</a>");
                out.println("</div>");
                out.println("</form>");
            } else {
                out.println("<h1>Error</h1>");
                out.println("<div class='error-message'>");
                out.println("<p>Quote not found for the given ID.</p>");
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

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Quote Updated</title>");
        out.println("<link rel='stylesheet' type='text/css' href='c.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");

        String newQuote = request.getParameter("quote");
        String quoteIdStr = request.getParameter("id");
        
        if (newQuote == null || quoteIdStr == null || quoteIdStr.isEmpty()) {
            out.println("<h1>Error</h1>");
            out.println("<div class='error-message'>");
            out.println("<p>Missing quote or quote ID.</p>");
            out.println("</div>");
            out.println("<a href='viewquotes' class='button'>Back to Quotes</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            return;
        }
        
        try {
            int quoteId = Integer.parseInt(quoteIdStr);

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/motivationdiary", "root", "root");
            String query = "UPDATE quotes SET quote = ? WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, newQuote);
            pstmt.setInt(2, quoteId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                out.println("<h1>Quote Updated</h1>");
                out.println("<div class='success-message'>");
                out.println("<svg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'><path d='M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z'/></svg>");
                out.println("<p>The quote was updated successfully.</p>");
                out.println("</div>");
            } else {
                out.println("<h1>Update Failed</h1>");
                out.println("<div class='error-message'>");
                out.println("<svg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'><path d='M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z'/></svg>");
                out.println("<p>Failed to update the quote.</p>");
                out.println("</div>");
            }

            out.println("<div class='form-group'>");
            out.println("<a href='viewquotes' class='button'>Back to Quotes</a>");
            out.println("</div>");

            pstmt.close();
            con.close();
        } catch (NumberFormatException e) {
            out.println("<h1>Error</h1>");
            out.println("<div class='error-message'>");
            out.println("<p>Invalid Quote ID.</p>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<a href='viewquotes' class='button'>Back to Quotes</a>");
            out.println("</div>");
        } catch (Exception e) {
            out.println("<h1>Error</h1>");
            out.println("<div class='error-message'>");
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