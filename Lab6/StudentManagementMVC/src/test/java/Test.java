import com.student.dao.UserDAO;
import com.student.model.User;

public class Test{
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();

        // Test authentication
        User user = dao.authenticate("Kien", "kienheo");
        if (user != null) {
            System.out.println("Authentication successful!");
            System.out.println(user);
        } else {
            System.out.println("Authentication failed!");
        }

        // Test with wrong password
        User invalidUser = dao.authenticate("admin", "wrongpassword");
        System.out.println("Invalid auth: " + (invalidUser == null ? "Correctly rejected" : "ERROR!"));
    }

}
