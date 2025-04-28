import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
public class login extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Login Result</title>");
        out.println("<link rel='stylesheet' type='text/css' href='c.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            out.println("<h1>Login Error</h1>");
            out.println("<div class='error-message'>");
            out.println("<p>Both fields are required!</p>");
            out.println("</div>");
            out.println("<a href='login.html' class='button'>Go back</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/motivationdiary", "root", "root");
            String query = "SELECT role FROM users WHERE name = ? AND password = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                HttpSession session = request.getSession(true);
                session.setAttribute("username", username);
                session.setAttribute("role", role);
                response.sendRedirect("viewquotes");
                return;
            } else {
                out.println("<h1>Login Failed</h1>");
                out.println("<div class='error-message'>");
                out.println("<p>Invalid username or password!</p>");
                out.println("</div>");
            }

            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            out.println("<h1>Login Error</h1>");
            out.println("<div class='error-message'>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("</div>");
        }

        out.println("<div class='form-group'>");
        out.println("<a href='login.html' class='button'>Try Again</a>");
        out.println("<a href='signup.html' class='button'>Sign Up</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}