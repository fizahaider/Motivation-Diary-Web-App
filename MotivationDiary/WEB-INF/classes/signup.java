import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
public class signup extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Sign Up Result</title>");
        out.println("<link rel='stylesheet' type='text/css' href='c.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");

        String username = request.getParameter("name");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmpassword");
        String role = request.getParameter("role");

        if (username == null || password == null || confirmPassword == null || role == null) {
            out.println("<h1>Sign Up Error</h1>");
            out.println("<div class='error-message'>");
            out.println("<p>All fields are required!</p>");
            out.println("</div>");
            out.println("<a href='signup.html' class='button'>Try Again</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            return;
        }

        if (!password.equals(confirmPassword)) {
            out.println("<h1>Sign Up Error</h1>");
            out.println("<div class='error-message'>");
            out.println("<p>Passwords do not match!</p>");
            out.println("</div>");
            out.println("<a href='signup.html' class='button'>Try Again</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://127.0.0.1:3306/motivationdiary";
            Connection con = DriverManager.getConnection(url, "root", "root");
            String query = "INSERT INTO users (name, password, role) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password); 
            pstmt.setString(3, role);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                HttpSession session = request.getSession(true);  
                response.sendRedirect("login.html");
                return;
            } else {
                out.println("<h1>Sign Up Error</h1>");
                out.println("<div class='error-message'>");
                out.println("<p>Could not complete sign-up. Please try again.</p>");
                out.println("</div>");
            }
            pstmt.close();
            con.close();

        } catch (Exception e) {
            out.println("<h1>Sign Up Error</h1>");
            out.println("<div class='error-message'>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("</div>");
        }

        out.println("<div class='form-group'>");
        out.println("<a href='signup.html' class='button'>Try Again</a>");
        out.println("<a href='login.html' class='button'>Login Instead</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}