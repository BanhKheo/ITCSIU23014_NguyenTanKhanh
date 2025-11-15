# 🚀 LAB 5 EXERCISES: SERVLET & MVC PATTERN
Course: Web Application Development
Name: Nguyen Tan Khanh
ID: ITCSIU23014
Tutor: Nguyen Trung Nghia

# PART A: IN-CLASS EXERCISES (60 points)

## Project Structure (5 points)
![alt text](img/projectStruture.png)

## Test DAO
![alt text](img/testDAO.png)

## Test Sequence

### List: Navigate to /student - should see existing students
## Student Display
![alt text](img/projectResults.png)
```
@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteStudent(request, response);
                break;
            default:
                listStudents(request, response);
                break;
        }
    }

```
> When browser point to server link, it sends the get request
> with no input the action defaults is list 
> ,then it call the listStudents function.
```
private void listStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    List<Student> students = studentDAO.getAllStudents();
    request.setAttribute("students", students);

    RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
    dispatcher.forward(request, response);
}
```
- listStudent is the handshake between Controller and View 
- request.setAttribute like a backpack attaching data to the current HTTP Request object
- You are creating a "navigator" object. You are telling the server: "I want to go to the file located at /views/student-list.jsp

### Add: Click "Add New Student"
![alt text](img/addStudent.png)

```
<div style="margin-bottom: 20px;">
    <a href="student?action=new" class="btn btn-primary">
        ➕ Add New Student
    </a>
</div>
```
- When click on add a tag it will call get method attach action = add 
- when action = add it call function showNewForm and request to access student-form.jsp
![alt text](img/addSuccessfull.png)
```
 <form action="student" method="POST">
    <!-- Hidden field for action -->
    <input type="hidden" name="action"
           value="${student != null ? 'update' : 'insert'}">

    <!-- Hidden field for ID (only for update) -->
    <c:if test="${student != null}">
        <input type="hidden" name="id" value="${student.id}">
    </c:if>
```
- When submit the form it call method post in StudentController
- If student = null set the action to insert
- Action insert in doPost call function 
```
private void insertStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

    String studentCode = request.getParameter("studentCode");
    String fullName = request.getParameter("fullName");
    String email = request.getParameter("email");
    String major = request.getParameter("major");

    Student newStudent = new Student(studentCode, fullName, email, major);

    if (studentDAO.addStudent(newStudent)) {
        response.sendRedirect("student?action=list&message=Student added successfully");
    } else {
        response.sendRedirect("student?action=list&error=Failed to add student");
    }
}
```
- Go to DAO and connect with database and insert the new student in the DB
- Then reDirect the link and do the list task above call get method again. If false with fail msg if true successfully msg

### Edit: Click "Edit" on test student
- The same with explanation in add
### Delete 
- The same with explanation in add







