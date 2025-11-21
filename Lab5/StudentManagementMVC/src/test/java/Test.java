import com.student.dao.StudentDAO;
import com.student.model.Student;

import java.util.List;

public class Test {
    // Add this main method to test (remove after testing)
    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();
        // Create a simple test in main() or controller
        List<Student> results = dao.searchStudents("khanh");
        System.out.println("Found " + results.size() + " students");
        for (Student s : results) {
            System.out.println(s);
        }
    }
}
