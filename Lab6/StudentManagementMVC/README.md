# 🚀 LAB 6 EXERCISES: AUTHENTICATION & SESSION MANAGEMENT
Course: Web Application Development
Name: Nguyen Tan Khanh
ID: ITCSIU23014
Tutor: Nguyen Trung Nghia

# PART A: IN-CLASS EXERCISES (60 points)

## Project Structure (5 points)
![alt text](img/projectStruture.png)

## EXERCISE 1: DATABASE & USER MODEL (15 points)

### Test Hash Function
![alt text](img/testHashFunction.png)

## EXERCISE 2: USER MODEL & DAO (15 points)
### Authentication core method
```
private static final String SQL_AUTHENTICATE =
            "SELECT * FROM users WHERE username = ? AND is_active = 1";
public User authenticate(String username, String password) {
    try (Connection conn = getConnection();
         // Use the CONSTANT defined at the top
         PreparedStatement pstmt = conn.prepareStatement(SQL_AUTHENTICATE)) {

        pstmt.setString(1, username);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                // 1. Get the hashed password from DB
                String storedHash = rs.getString("password");

                // 2. Check password using BCrypt
                if (BCrypt.checkpw(password, storedHash)) {

                    // 3. Use Helper method to create User object
                    User user = mapResultSetToUser(rs);

                    // Security: Clear the password hash before returning to Controller/View
                    user.setPassword(null);

                    // 4. Update last login
                    updateLastLogin(user.getId());

                    return user;
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // Login failed
}
```
- Access table user in database retrieve the user with username
- Using the Bcrypt library to check password and the hash password store in database is that correct or not 
- Then store all attribute of the row to user object

### Authentication Test
![alt text](img/testAuthentication.png)


## EXERCISE 3: LOGIN/LOGOUT CONTROLLERS (15 points)
![alt text](img/loginUI.png)

### Retrive DashBoard and Student List without login
- http://localhost:8080/StudentManagementMVC/dashboard
```
// 1. Get user from session
HttpSession session = request.getSession(false);
User user = (session != null) ? (User) session.getAttribute("user") : null;

// Security Check: If not logged in, redirect to login
if (user == null) {
    response.sendRedirect("login?error=Session expired, please login again");
    return;
}

// 2. Get statistics (Total Students)
// Ensure your StudentDAO has the getTotalStudents() method!
int totalStudents = studentDAO.getTotalStudents();

// 3. Set attributes
request.setAttribute("totalStudents", totalStudents);

// 4. Forward to dashboard.jsp
request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
```
- This code using to check is there any section has already generate if not require user to login 
- If already login move to the dashboard page
### Login Successfull
![alt text](img/loginSuccessfull.png)


