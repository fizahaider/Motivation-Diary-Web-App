import java.io.*;

public class personInfo {
     private String name;      
    private String password;  
    private String role;      

    public personInfo(){
        name = "";
        password = "";
        role = "user";
    }
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
